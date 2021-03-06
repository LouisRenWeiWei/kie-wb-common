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

package org.kie.workbench.common.stunner.core.client.canvas.controls.toolbox;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvas;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.canvas.event.registration.CanvasShapeRemovedEvent;
import org.kie.workbench.common.stunner.core.client.canvas.event.selection.CanvasClearSelectionEvent;
import org.kie.workbench.common.stunner.core.client.canvas.event.selection.CanvasElementSelectedEvent;
import org.kie.workbench.common.stunner.core.client.components.toolbox.actions.ActionsToolboxFactory;
import org.kie.workbench.common.stunner.core.client.shape.Shape;
import org.kie.workbench.common.stunner.core.graph.Edge;
import org.kie.workbench.common.stunner.core.graph.Element;
import org.kie.workbench.common.stunner.core.graph.Node;
import org.kie.workbench.common.stunner.core.graph.content.view.View;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ToolboxControlTest {

    @Mock
    private AbstractCanvasHandler canvasHandler;

    @Mock
    private AbstractCanvas canvas;

    @Mock
    private Node<View<?>, Edge> element;

    @Mock
    private ActionsToolboxFactory toolboxFactory;

    @Mock
    private ToolboxControlImpl<ActionsToolboxFactory> delegated;

    private AbstractToolboxControl tested;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() throws Exception {
        when(canvasHandler.getCanvas()).thenReturn(canvas);
        when(delegated.getCanvasHandler()).thenReturn(canvasHandler);
        this.tested = new AbstractToolboxControl(delegated) {
            @Override
            protected List<ActionsToolboxFactory> getFactories() {
                return Collections.singletonList(toolboxFactory);
            }
        };
    }

    @Test
    public void testEnable() {
        tested.enable(canvasHandler);
        verify(delegated,
               times(1)).enable(eq(canvasHandler));
    }

    @Test
    public void testRegister() {
        tested.enable(canvasHandler);
        tested.register(element);
        verify(delegated,
               times(1)).register(eq(element));
        verify(delegated,
               never()).deregister(any(Element.class));
    }

    @Test
    public void testDeRegister() {
        tested.enable(canvasHandler);
        tested.deregister(element);
        verify(delegated,
               times(1)).deregister(eq(element));
        verify(delegated,
               never()).register(any(Element.class));
    }

    @Test
    public void testDisable() {
        tested.disable();
        verify(delegated,
               times(1)).disable();
    }

    @Test
    public void testGetToolboxes() {
        tested.getToolboxes(element);
        verify(delegated,
               times(1)).getToolboxes(eq(element));
    }

    @Test
    public void testElementSelectedEvent() {
        final String uuid = "uuid1";
        when(element.getUUID()).thenReturn(uuid);
        final CanvasElementSelectedEvent event = new CanvasElementSelectedEvent(canvasHandler,
                                                                                element.getUUID());
        tested.onCanvasElementSelectedEvent(event);
        verify(delegated,
               times(1)).show(eq(uuid));
        verify(delegated,
               never()).destroy();
    }

    @Test
    public void testClearSelectionEvent() {
        final CanvasClearSelectionEvent event = new CanvasClearSelectionEvent(canvasHandler);
        tested.onCanvasClearSelectionEvent(event);
        verify(delegated,
               times(1)).destroy();
        verify(delegated,
               never()).show(any(Element.class));
        verify(delegated,
               never()).show(anyString());
    }

    @Test
    public void testShapeRemovedEvent() {
        final String uuid = "uuid1";
        when(delegated.isActive(eq(uuid))).thenReturn(true);
        final Shape shape = mock(Shape.class);
        when(shape.getUUID()).thenReturn(uuid);
        final CanvasShapeRemovedEvent event = new CanvasShapeRemovedEvent(canvas,
                                                                          shape);
        tested.onCanvasShapeRemovedEvent(event);
        verify(delegated,
               times(1)).destroy();
        verify(delegated,
               never()).show(any(Element.class));
        verify(delegated,
               never()).show(anyString());
    }
}
