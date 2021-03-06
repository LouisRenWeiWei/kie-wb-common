/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.bpmn.definition.property.task;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
import org.kie.workbench.common.stunner.bpmn.definition.ExclusiveDatabasedGateway;
import org.kie.workbench.common.stunner.bpmn.definition.property.general.Name;

import static org.junit.Assert.assertTrue;

public class ExclusiveDatabasedGatewayTest {

    private Validator validator;

    private static final String NAME_VALID = "Gateway";

    @Before
    public void init() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
    }

    @Test
    public void testExclusiveDatabasedGatewayNameValid() {
        ExclusiveDatabasedGateway exclusiveDatabasedGateway = new ExclusiveDatabasedGateway.ExclusiveDatabasedGatewayBuilder().build();
        exclusiveDatabasedGateway.getGeneral().setName(new Name(NAME_VALID));
        Set<ConstraintViolation<ExclusiveDatabasedGateway>> violations = this.validator.validate(exclusiveDatabasedGateway);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testExclusiveDatabasedGatewayNameEmpty() {
        ExclusiveDatabasedGateway exclusiveDatabasedGateway = new ExclusiveDatabasedGateway.ExclusiveDatabasedGatewayBuilder().build();
        exclusiveDatabasedGateway.getGeneral().setName(new Name(""));
        Set<ConstraintViolation<ExclusiveDatabasedGateway>> violations = this.validator.validate(exclusiveDatabasedGateway);
        assertTrue(violations.isEmpty());
    }
}
