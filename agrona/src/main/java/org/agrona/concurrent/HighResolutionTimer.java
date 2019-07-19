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

/**
 * Control the use of high resolution timers on Windows by a bit of hackery.
 */
public class HighResolutionTimer
{
    private static Thread thread;

    /**
     * Has the high resolution timer been enabled?
     *
     * @return true if the we believe it is enabled otherwise false.
     */
    public static boolean isEnabled()
    {
        return null != thread;
    }

    /**
     * Attempt to enable high resolution timers.
     */
    public static synchronized void enable()
    {
        if (null == thread)
        {
            thread = new Thread(HighResolutionTimer::run);
            thread.setDaemon(true);
            thread.setName("high-resolution-timer-hack");
            thread.start();
        }
    }

    /**
     * Attempt to disable the high resolution timers.
     */
    public static synchronized void disable()
    {
        if (null != thread)
        {
            thread.interrupt();
            thread = null;
        }
    }

    private static void run()
    {
        try
        {
            Thread.sleep(Long.MAX_VALUE);
        }
        catch (final InterruptedException ignore)
        {
        }

        thread = null;
    }
}