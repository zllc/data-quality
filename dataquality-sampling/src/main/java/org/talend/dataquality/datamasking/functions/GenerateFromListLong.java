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
package org.talend.dataquality.datamasking.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * created by jgonzalez on 24 juin 2015. See GenerateFromList.
 *
 */
public class GenerateFromListLong extends GenerateFromList<Long> {

    private static final long serialVersionUID = 1753229433319188154L;

    private List<Long> LongTokens = new ArrayList<>();

    @Override
    public void parse(String extraParameter, boolean keepNullValues, Random rand) {
        super.parse(extraParameter, keepNullValues, rand);
        super.init();
    }

    @Override
    protected Long doGenerateMaskedField(Long l) {
        for (int j = 0; j < substituteList.size(); ++j) {
            long tmp = 0L;
            try {
                tmp = Long.parseLong(substituteList.get(j));
            } catch (NumberFormatException e) {
                // Do Nothing
            }
            LongTokens.add(tmp);
        }
        if (LongTokens.size() > 0) {
            return LongTokens.get(rnd.nextInt(LongTokens.size()));
        } else {
            return 0L;
        }
    }
}
