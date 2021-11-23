/*
 * Copyright (c) 2008-2010, Piccolo2D project, http://piccolo2d.org
 * Copyright (c) 1998-2008, University of Maryland
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * None of the name of the University of Maryland, the name of the Piccolo2D project, or the names of its
 * contributors may be used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.umd.cs.piccolo;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.piccolo.util.PBounds;

/**
 * Mock PCamera.
 */
class MockPCamera extends PCamera {
    private static final long serialVersionUID = 1L;
    private final List notifications = new ArrayList();

    public void repaintFromLayer(final PBounds bounds, final PLayer layer) {
        notifications.add(new Notification("repaintFromLayer", bounds, layer));
        super.repaintFromLayer(bounds, layer);
    }

    static class Notification {
        private final String type;
        private final PBounds bounds;
        // this should really be PLayer
        private final PNode layer;

        private Notification(final String type, final PBounds bounds, final PNode layer) {
            this.bounds = bounds;
            this.layer = layer;
            this.type = type;
        }

        public PNode getLayer() {
            return layer;
        }

        public PBounds getBounds() {
            return bounds;
        }
    }

    public int getNotificationCount() {
        return notifications.size();
    }

    public Notification getNotification(final int i) {
        return (Notification) notifications.get(i);
    }

}