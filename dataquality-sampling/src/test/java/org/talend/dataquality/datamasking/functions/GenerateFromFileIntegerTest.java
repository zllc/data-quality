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

import java.net.URISyntaxException;

import org.junit.Test;
import org.talend.dataquality.duplicating.RandomWrapper;

/**
 * created by jgonzalez on 30 juin 2015 Detailled comment
 *
 */
public class GenerateFromFileIntegerTest {

    private GenerateFromFileInteger gffi = new GenerateFromFileInteger();

    @Test
    public void testGood() throws URISyntaxException {
        String path = this.getClass().getResource("data/numbers.txt").toURI().getPath(); //$NON-NLS-1$
        gffi.parse(path, false, new RandomWrapper(42));
        assertEquals(9, gffi.generateMaskedRow(0).intValue());
    }

    @Test
    public void testNull() {
        gffi.parse(Function.EMPTY_STRING, false, new RandomWrapper(42));
        gffi.setKeepNull(true);
        assertEquals(0, gffi.generateMaskedRow(0).intValue());
        assertEquals(null, gffi.generateMaskedRow(null));
    }
}
