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
package org.agrona.concurrent;

import org.agrona.concurrent.status.StatusIndicatorReader;
import org.agrona.hints.ThreadHints;

import java.util.concurrent.locks.LockSupport;

/**
 * {@link IdleStrategy} which can be controlled by a counter so its mode of operation can be switched between
 * doing nothing (NOOP), busy spinning by calling {@link ThreadHints#onSpinWait()}, yielding by calling
 * {@link Thread#yield()}, or sleeping for the minimum period by calling {@link LockSupport#parkNanos(long)} when
 * work count is zero so it idles.
 */
public final class ControllableIdleStrategy implements IdleStrategy
{
    public static final int NOT_CONTROLLED = 0;
    public static final int NOOP = 1;
    public static final int BUSY_SPIN = 2;
    public static final int YIELD = 3;
    public static final int PARK = 4;

    private static final long PARK_PERIOD_NANOSECONDS = 1000;

    private final StatusIndicatorReader statusIndicator;

    public ControllableIdleStrategy(final StatusIndicatorReader statusIndicator)
    {
        this.statusIndicator = statusIndicator;
    }

    /**
     * Idle based on current status indication value
     *
     * @param workCount performed in last duty cycle.
     * @see IdleStrategy#idle(int)
     */
    public void idle(final int workCount)
    {
        if (workCount > 0)
        {
            return;
        }

        idle();
    }

    /**
     * {@inheritDoc}
     */
    public void idle()
    {
        final int status = (int)statusIndicator.getVolatile();

        switch (status)
        {
            case NOOP:
                break;

            case BUSY_SPIN:
                ThreadHints.onSpinWait();
                break;

            case YIELD:
                Thread.yield();
                break;

            case PARK:
            default:
                LockSupport.parkNanos(PARK_PERIOD_NANOSECONDS);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void reset()
    {
    }

    public String toString()
    {
        return "ControllableIdleStrategy{" +
            "statusIndicator=" + statusIndicator +
            '}';
    }
}
