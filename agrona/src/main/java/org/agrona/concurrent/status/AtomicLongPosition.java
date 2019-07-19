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

import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link Position} that is backed by an {@link AtomicLong} which is useful for tests.
 */
public class AtomicLongPosition extends Position
{
    private boolean isClosed = false;
    private final int id;
    private final AtomicLong value;

    public AtomicLongPosition()
    {
        this(0, 0L);
    }

    public AtomicLongPosition(final int id)
    {
        this(id, 0L);
    }

    public AtomicLongPosition(final int id, final long initialValue)
    {
        this.id = id;
        this.value = new AtomicLong(initialValue);
    }

    public boolean isClosed()
    {
        return isClosed;
    }

    public int id()
    {
        return id;
    }

    public long get()
    {
        return value.get();
    }

    public long getVolatile()
    {
        return value.get();
    }

    public void set(final long value)
    {
        this.value.lazySet(value);
    }

    public void setOrdered(final long value)
    {
        this.value.lazySet(value);
    }

    public void setVolatile(final long value)
    {
        this.value.set(value);
    }

    public boolean proposeMax(final long proposedValue)
    {
        return proposeMaxOrdered(proposedValue);
    }

    public boolean proposeMaxOrdered(final long proposedValue)
    {
        boolean updated = false;

        if (get() < proposedValue)
        {
            setOrdered(proposedValue);
            updated = true;
        }

        return updated;
    }

    public void close()
    {
        isClosed = true;
    }

    public String toString()
    {
        return "AtomicLongPosition{" +
            "isClosed=" + isClosed +
            ", id=" + id +
            ", value=" + value +
            '}';
    }
}
