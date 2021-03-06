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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.talend.dataquality.datamasking.functions.GenerateFromListHashString;
import org.talend.dataquality.duplicating.RandomWrapper;

/**
 * created by jgonzalez on 29 juin 2015 Detailled comment
 *
 */
public class GenerateFromListHashStringTest {

    private String output;

    private GenerateFromListHashString gflhs = new GenerateFromListHashString();

    @Before
    public void setUp() throws Exception {
        gflhs.parse("Toto, Titi, Tata", false, new RandomWrapper(42)); //$NON-NLS-1$
    }

    @Test
    public void testGood() {
        output = gflhs.generateMaskedRow(null);
        assertEquals(output, "Tata"); //$NON-NLS-1$
    }

    @Test
    public void testNull() {
        gflhs.keepNull = true;
        output = gflhs.generateMaskedRow(null);
        assertEquals(output, null);
    }
}
