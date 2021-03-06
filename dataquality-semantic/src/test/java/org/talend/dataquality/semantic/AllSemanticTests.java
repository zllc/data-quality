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
package org.talend.dataquality.semantic;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.talend.dataquality.semantic.classifier.custom.UDCategorySerDeserTest;
import org.talend.dataquality.semantic.classifier.custom.UserDefinedClassifierTest;
import org.talend.dataquality.semantic.classifier.custom.UserDefinedRegexValidatorTest;
import org.talend.dataquality.semantic.validator.impl.SedolValidatorTest;

/**
 * DOC yyin class global comment. Detailled comment
 */
@RunWith(Suite.class)
@SuiteClasses({ CategoryRecognizerTest.class, RespectiveCategoryRecognizerTest.class, UserDefinedClassifierTest.class,
        UserDefinedRegexValidatorTest.class, UDCategorySerDeserTest.class, SedolValidatorTest.class })
public class AllSemanticTests {

}
