/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ikhiloyaimokhai.workmanagersyncremotedata.util;

public final class Constants {

    // Name of Notification Channel for verbose notifications of background work
    public static final CharSequence VERBOSE_NOTIFICATION_CHANNEL_NAME =
            "Verbose WorkManager Notifications";
    public static final String KEY_OUTPUT_DATA = "KEY_OUTPUT_DATA";
    public static String VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
            "Shows notifications whenever work starts";
    public static final CharSequence NOTIFICATION_TITLE = "WorkRequest Starting";
    public static final String CHANNEL_ID = "VERBOSE_NOTIFICATION";
    public static final int NOTIFICATION_ID = 1;

    // The name of the Sync Data work
    public static final String SYNC_DATA_WORK_NAME = "sync_data_work_name";


    // Other keys
    public static final long DELAY_TIME_MILLIS = 3000;

    public static final String TAG_SYNC_DATA = "TAG_SYNC_DATA";

    // Ensures this class is never instantiated
    private Constants() {
    }
}