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

/**
 * created by jgonzalez on 22 juin 2015. See RemoveFirstChars.
 *
 */
public class RemoveFirstCharsLong extends RemoveFirstChars<Long> {

    private static final long serialVersionUID = -4656535468430704086L;

    @Override
    protected Long doGenerateMaskedField(Long l) {
        if (l != null && (int) Math.log10(l) + 1 > integerParam && integerParam > 0) {
            return Long.parseLong(l.toString().substring(integerParam));
        } else {
            return 0L;
        }
    }
}
