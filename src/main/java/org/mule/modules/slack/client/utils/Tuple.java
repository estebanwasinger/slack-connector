/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.client.utils;

import java.io.Serializable;

public class Tuple<X, Y> implements Serializable{

    public X getLeft() {
        return left;
    }

    public Y getRight() {
        return right;
    }

    public final X left;
    public final Y right;

    public Tuple(X left, Y right) {
        this.left = left;
        this.right = right;
    }
}