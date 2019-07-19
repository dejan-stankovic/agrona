/*
 * Copyright 2014-2019 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.agrona.concurrent.status;

/**
 * Extends a {@link StatusIndicatorReader} with the ability to set the value so other readers can take action.
 * @see CountersManager
 */
public abstract class StatusIndicator extends StatusIndicatorReader
{
    /**
     * Sets the current status indication of the component with ordered atomic memory semantics.
     *
     * @param value the current status indication of the component.
     */
    public abstract void setOrdered(long value);
}
