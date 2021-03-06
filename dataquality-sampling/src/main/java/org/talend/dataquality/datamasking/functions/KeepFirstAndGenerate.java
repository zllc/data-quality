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
 * created by jgonzalez on 22 juin 2015. This function will keep the n first digits of the input and then replace all
 * the digits that come after by other digits. Anything that is not a digit will be kept.
 *
 */
public abstract class KeepFirstAndGenerate<T2> extends Function<T2> {

    private static final long serialVersionUID = 2523362343532887640L;

    @Override
    protected abstract T2 doGenerateMaskedField(T2 t);
}
