/*
 * Copyright (c) 2019-2023 Tomasz Babiuk
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

import eu.automateeverything.blinkyplugin.BlinkerConfigurable.Companion.STATE_BLINKING
import eu.automateeverything.blinkyplugin.BlinkerConfigurable.Companion.STATE_OFF
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.domain.hardware.Port
import eu.automateeverything.domain.hardware.Relay
import java.lang.Exception
import java.util.Calendar
import kotlin.Throws

class BlinkerAutomationUnit(
    eventBus: EventBus,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val controlPort: Port<Relay>,
    private val interval: Int
) : StateDeviceAutomationUnitBase(eventBus, instance, name, ControlType.States, states, false) {

    @Throws(Exception::class)
    override fun applyNewState(state: String) {
        if (currentState.id == STATE_BLINKING) {
            changeRelayStateIfNeeded(controlPort, Relay.ON)
        } else if (currentState.id == STATE_OFF) {
            changeRelayStateIfNeeded(controlPort, Relay.OFF)
        }
    }

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.portId)

    override fun calculateInternal(now: Calendar) {
        if (controlPort.read() == Relay.ON) {
            changeState(STATE_BLINKING, null)
        } else {
            changeState(STATE_OFF, null)
        }
    }

    init {
        calculateInternal(Calendar.getInstance())
    }

    override val recalculateOnTimeChange = true
    override val recalculateOnPortUpdate = false
}
