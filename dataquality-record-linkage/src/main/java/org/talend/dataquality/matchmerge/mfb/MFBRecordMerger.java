// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.matchmerge.mfb;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.talend.dataquality.matchmerge.Attribute;
import org.talend.dataquality.matchmerge.AttributeValues;
import org.talend.dataquality.matchmerge.Record;
import org.talend.dataquality.record.linkage.record.IRecordMerger;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;

public class MFBRecordMerger implements IRecordMerger {

    protected SurvivorShipAlgorithmEnum[] typeMergeTable;

    protected String[] parameters;

    protected final String mergedRecordSource;

    public MFBRecordMerger(String mergedRecordSource, String[] parameters, SurvivorShipAlgorithmEnum[] typeMergeTable) {
        this.mergedRecordSource = mergedRecordSource;
        this.parameters = parameters;
        this.typeMergeTable = typeMergeTable;
    }

    @Override
    public Record merge(Record record1, Record record2) {
        List<Attribute> r1 = record1.getAttributes();
        List<Attribute> r2 = record2.getAttributes();
        // Takes most recent as timestamp for the merged record.
        long mergedRecordTimestamp = record1.getTimestamp() > record2.getTimestamp() ? record1.getTimestamp()
                : record2.getTimestamp();
        Record mergedRecord = createNewRecord(record1, record2, mergedRecordTimestamp);
        for (int k = 0; k < r1.size(); k++) {
            Attribute a = new Attribute(r1.get(k).getLabel(), r1.get(k).getColumnIndex());
            mergedRecord.getAttributes().add(k, a);
        }
        for (int i = 0; i < r1.size(); i++) {
            Attribute mergedAttribute = mergedRecord.getAttributes().get(i);
            Attribute leftAttribute = r1.get(i);
            Attribute rightAttribute = r2.get(i);
            String leftValue = leftAttribute.getValue();
            String rightValue = rightAttribute.getValue();
            // Keep values from original records (if any)
            AttributeValues<String> leftValues = leftAttribute.getValues();
            if (leftValues.size() > 0) {
                mergedAttribute.getValues().merge(leftValues);
            } else {
                mergedAttribute.getValues().get(leftValue).increment();
            }
            AttributeValues<String> rightValues = rightAttribute.getValues();
            if (rightValues.size() > 0) {
                mergedAttribute.getValues().merge(rightValues);
            } else {
                mergedAttribute.getValues().get(rightValue).increment();
            }
            // Merge values
            if (leftValue == null && rightValue == null) {
                mergedAttribute.setValue(null);
            } else {
                String mergedValue = createMergeValue(record1.getSource(), record2.getSource(), parameters[i],
                        record1.getTimestamp(), record2.getTimestamp(), typeMergeTable[i], leftValue, rightValue,
                        mergedAttribute.getValue(), mergedAttribute.getValues());
                if (mergedValue != null) {
                    mergedAttribute.setValue(mergedValue);
                }
            }
        }
        mergedRecord.setRelatedIds(new HashSet<String>(record1.getRelatedIds().size() + record2.getRelatedIds().size() + 2));
        mergedRecord.getRelatedIds().add(record1.getId());
        mergedRecord.getRelatedIds().add(record2.getId());
        mergedRecord.getRelatedIds().addAll(record1.getRelatedIds());
        mergedRecord.getRelatedIds().addAll(record2.getRelatedIds());
        // Conservative strategy -> keeps the lowest confidence to avoid over-confidence in a group with many
        // low-confidence records.
        mergedRecord.setConfidence(Math.min(record1.getConfidence(), record2.getConfidence()));
        if (record1.getGroupId() != null) {
            mergedRecord.setGroupId(record1.getGroupId());
        } else if (record2.getGroupId() != null) {
            mergedRecord.setGroupId(record2.getGroupId());
        }
        return mergedRecord;
    }

    /**
     * Create a merged value given the values from record 1 and record 2.
     * 
     * @return the merged value.
     */
    protected String createMergeValue(String leftSource, String rightSource, String parameter, long leftTimeStamp,
            long rightTimeStamp, SurvivorShipAlgorithmEnum survivorShipAlgorithmEnum, String leftValue, String rightValue,
            String mergedValue, AttributeValues<String> mergedValues) {
        BigDecimal leftNumberValue;
        BigDecimal rightNumberValue;
        int leftValueLength = leftValue == null ? 0 : leftValue.length();
        int rightValueLength = rightValue == null ? 0 : rightValue.length();
        switch (survivorShipAlgorithmEnum) {
        case CONCATENATE:
            if (StringUtils.isEmpty(parameter)) {
                return leftValue + rightValue;
            } else {
                return leftValue + parameter + rightValue;
            }
        case LARGEST:
            leftNumberValue = parseNumberValue(leftValue);
            rightNumberValue = parseNumberValue(rightValue);
            if (leftNumberValue == null) {
                return rightValue;
            }
            if (rightNumberValue == null) {
                return leftValue;
            }
            if (leftNumberValue.compareTo(rightNumberValue) >= 0) {
                return leftValue;
            } else {
                return rightValue;
            }
        case SMALLEST:
            leftNumberValue = parseNumberValue(leftValue);
            rightNumberValue = parseNumberValue(rightValue);
            if (leftNumberValue == null) {
                return rightValue;
            }
            if (rightNumberValue == null) {
                return leftValue;
            }
            if (leftNumberValue.compareTo(rightNumberValue) <= 0) {
                return leftValue;
            } else {
                return rightValue;
            }
        case MOST_RECENT:
            if (leftTimeStamp > rightTimeStamp) {
                return leftValue;
            } else if (leftTimeStamp < rightTimeStamp) {
                return rightValue;
            } else {
                // Both r1 and r2 have same timestamp, return first value
                return leftValue;
            }
        case MOST_ANCIENT:
            if (leftTimeStamp < rightTimeStamp) {
                return leftValue;
            } else if (leftTimeStamp > rightTimeStamp) {
                return rightValue;
            } else {
                // Both r1 and r2 have same timestamp, return first value
                return leftValue;
            }
        case PREFER_TRUE:
            if (Boolean.parseBoolean(leftValue) || Boolean.parseBoolean(rightValue)) {
                return "true"; //$NON-NLS-1$
            } else {
                return "false"; //$NON-NLS-1$
            }
        case PREFER_FALSE:
            if (Boolean.parseBoolean(leftValue) && Boolean.parseBoolean(rightValue)) {
                return "true"; //$NON-NLS-1$
            } else {
                return "false"; //$NON-NLS-1$
            }
        case MOST_COMMON:
            return mergedValues.mostCommon();
        case LONGEST:
            if (leftValueLength > rightValueLength) {
                return leftValue;
            } else if (leftValueLength < rightValueLength) {
                return rightValue;
            } else {
                // Same length and equals or same length
                return leftValue;
            }
        case SHORTEST:
            if (leftValueLength < rightValueLength) {
                return leftValue;
            } else if (leftValueLength > rightValueLength) {
                return rightValue;
            } else {
                // Same length and equals or same length
                return leftValue;
            }
        case MOST_TRUSTED_SOURCE:
            String mostTrustedSourceName = parameter;
            if (mostTrustedSourceName == null) {
                throw new IllegalStateException("Survivorship 'most trusted source' must specify a trusted source."); //$NON-NLS-1$
            }
            if (mostTrustedSourceName.equals(leftSource)) {
                return leftValue;
            } else if (mostTrustedSourceName.equals(rightSource)) {
                return rightValue;
            } else {
                // r1 and r2 are not from a trusted source, return first value
                return leftValue;
            }
        }
        return null;

    }

    /**
     * Create a new record given record1's id and merged record's timestamp.
     * 
     * @param record1
     * @param mergedRecordTimestamp
     * @return
     */
    protected Record createNewRecord(Record record1, Record record2, long mergedRecordTimestamp) {
        return new Record(record1.getId(), mergedRecordTimestamp, mergedRecordSource);
    }

    private static BigDecimal parseNumberValue(String value) {
        try {
            return value == null || value.isEmpty() ? null : new BigDecimal(value);
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }
}
