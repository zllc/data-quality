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
import org.talend.dataquality.duplicating.RandomWrapper;

/**
 * @author jteuladedenantes
 */
public class GenerateUniqueSsnFrTest {

    private String output;

    private GenerateUniqueSsnFr gnf = new GenerateUniqueSsnFr();

    @Before
    public void setUp() throws Exception {
        gnf.setRandomWrapper(new RandomWrapper(42));
    }

    @Test
    public void testKeepInvalidPatternTrue() {
        gnf.setKeepInvalidPattern(true);
        output = gnf.generateMaskedRow(null);
        assertEquals(output, null);
        output = gnf.generateMaskedRow("");
        assertEquals(output, "");
        output = gnf.generateMaskedRow("AHDBNSKD");
        assertEquals(output, "AHDBNSKD");
    }

    @Test
    public void testKeepInvalidPatternFalse() {
        gnf.setKeepInvalidPattern(false);
        output = gnf.generateMaskedRow(null);
        assertEquals(output, null);
        output = gnf.generateMaskedRow("");
        assertEquals(output, null);
        output = gnf.generateMaskedRow("AHDBNSKD");
        assertEquals(output, null);
    }

    @Test
    public void testGood() {
        output = gnf.generateMaskedRow("1860348282074 19");
        assertEquals(output, "2550348184792 24");
        // with spaces
        output = gnf.generateMaskedRow("2 12 12 15 953 006   88");
        assertEquals(output, "1 13 07 38 154 671   92");
        // corse department
        output = gnf.generateMaskedRow("10501 2B 532895 34");
        assertEquals(output, "23210 35 639916 62");
    }

    @Test
    public void testWrongSsnField() {
        gnf.setKeepInvalidPattern(false);
        // without a number
        output = gnf.generateMaskedRow("186034828207 19");
        assertEquals(output, null);
        // with a wrong letter
        output = gnf.generateMaskedRow("186034Y282079 19");
        assertEquals(output, null);
        // with a letter instead of a number
        output = gnf.generateMaskedRow("1860I48282079 19");
        assertEquals(output, null);
    }

}