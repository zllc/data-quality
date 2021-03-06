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
import org.talend.dataquality.datamasking.functions.GenerateCreditCard;
import org.talend.dataquality.datamasking.functions.GenerateCreditCardFormatLong;
import org.talend.dataquality.duplicating.RandomWrapper;

/**
 * created by jgonzalez on 29 juin 2015 Detailled comment
 *
 */
public class GenerateCreditCardFormatLongTest {

    private String output;

    private GenerateCreditCardFormatLong gccfl = new GenerateCreditCardFormatLong();

    @Before
    public void setUp() throws Exception {
        gccfl.setRandomWrapper(new RandomWrapper(42));
    }

    @Test
    public void testGood() {
        Long input = 4120356987563L;
        output = gccfl.generateMaskedRow(input).toString();
        assertEquals(output, String.valueOf(4038405589322L));
    }

    @Test
    public void testCheck() {
        gccfl.setRandomWrapper(new RandomWrapper());
        boolean res = true;
        for (int i = 0; i < 10; ++i) {
            Long tmp = gccfl.generateMaskedRow(4038405589322L);
            res = GenerateCreditCard.luhnTest(new StringBuilder(tmp.toString()));
            assertEquals("Wrong number : " + tmp, res, true); //$NON-NLS-1$
        }
    }

    @Test
    public void testBad() {
        output = gccfl.generateMaskedRow(null).toString();
        assertEquals(output, "4384055893226268");
    }
}
