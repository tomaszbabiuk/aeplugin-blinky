/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.blinkyplugin

import eu.automateeverything.data.localization.Resource

object R {
    val field_interval_hint = Resource(
        "Interval [s]",
        "Interwał [s]"
    )

    val field_port_hint = Resource(
        "Port",
        "Port"
    )

    val configurable_blinker_edit = Resource(
        "Edit blinker",
        "Edytuj migacz (sygnalizator)"
    )
    val configurable_blinker_add = Resource(
        "Add blinker",
        "Dodaj blinker"
    )

    val plugin_name = Resource(
        "Blinker",
        "Migacz"
    )

    val plugin_description = Resource(
        "Contains blinker device. The blinker turns the relay port on and off according to a predefined interval.",
        "Zawiera urzączenie migacz (sygnalizator). Migacz włącza i wyłącza port przekaźnika zgodnie ze zdefiniowanym interwałem."
    )

    val configurable_blinker_title = Resource(
        "Blinker",
        "Migacz"
    )

    val configurable_blinker_description = Resource(
        "The blinker turns the relay port on and off according to a predefined interval.",
        "Migacz włącza i wyłącza port przekaźnika zgodnie ze zdefiniowanym interwałem."
    )

    val state_unknown = Resource(
        "Unknown",
        "Nieznany"
    )

    val state_blinking = Resource(
        "Blinking",
        "Miganie"
    )

    val action_blinking = Resource(
        "Blink",
        "Migaj"
    )

    val state_off = Resource(
        "Off",
        "Wył"
    )

    val action_off = Resource(
        "Off",
        "Wył"
    )
}