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
package org.talend.dataquality.record.linkage.attribute;

import org.talend.dataquality.record.linkage.constant.AttributeMatcherType;
import org.talend.dataquality.record.linkage.utils.StringComparisonUtil;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class JaroMatcher extends AbstractAttributeMatcher {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.attribute.IAttributeMatcher#getMatchType()
     */
    public AttributeMatcherType getMatchType() {
        return AttributeMatcherType.JARO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.attribute.AbstractAttributeMatcher#getWeight(java.lang.String,
     * java.lang.String)
     */
    public double getWeight(String string1, String string2) {

        // get half the length of the string rounded up - (this is the distance used for acceptable transpositions)
        final int halflen = ((Math.min(string1.length(), string2.length())) / 2)
                + ((Math.min(string1.length(), string2.length())) % 2);

        // get common characters
        final StringBuffer common1 = StringComparisonUtil.getCommonCharacters(string1, string2, halflen);
        final StringBuffer common2 = StringComparisonUtil.getCommonCharacters(string2, string1, halflen);

        // check for zero in common
        if (common1.length() == 0 || common2.length() == 0) {
            return 0.0f;
        }

        // check for same length common strings returning 0.0f is not the same
        if (common1.length() != common2.length()) {
            return 0.0f;
        }

        // get the number of transpositions
        int transpositions = 0;
        for (int i = 0; i < common1.length(); i++) {
            if (common1.charAt(i) != common2.charAt(i))
                transpositions++;
        }
        transpositions /= 2.0f;

        // calculate jaro metric
        return (common1.length() / ((float) string1.length()) + common2.length() / ((float) string2.length())
                + (common1.length() - transpositions) / ((float) common1.length())) / 3.0f;
    }

}
