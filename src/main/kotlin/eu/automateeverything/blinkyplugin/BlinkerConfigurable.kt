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

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.devices.DevicesConfigurable
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.domain.hardware.PortFinder
import eu.automateeverything.domain.hardware.Relay
import org.pf4j.Extension
import java.math.BigDecimal
import java.util.HashMap

@Extension
class BlinkerConfigurable(
    private val eventBus: EventBus,
    private val portFinder: PortFinder) : StateDeviceConfigurable() {

    override val titleRes: Resource
        get() = R.configurable_blinker_title

    override val descriptionRes: Resource
        get() = R.configurable_blinker_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
             <g class="layer">
              <title>Layer 1</title>
              <path d="m89.38897,39.371l-4.331,0l-1.188,3.482c-1.242,-3.874 -4.398,-4.73 -6.366,-4.73c-2.762,-0.063 -5.787,1.381 -7.235,2.43c-3.871,2.952 -6.365,7.821 -6.365,12.486c0,4.859 3.542,9.525 8.344,9.525c0.851,0 1.837,-0.131 2.821,-0.463c2.896,-0.984 4.009,-2.626 4.799,-3.35c0.325,3.414 3.937,3.943 6.239,3.943c6.569,0 14.393,-7.227 13.868,-17.741c-0.527,-10.575 -11.109,-17.675 -20.964,-18.134c-2.003,-0.092 -3.953,0.044 -5.835,0.379l0,-0.829l-73.176,0l0,47.262l73.176,0l0,-0.524c0.717,0.066 1.437,0.103 2.154,0.103c4.271,0 9.137,-1.116 12.29,-2.236l-1.515,-3.545c-12.878,4.988 -27.99,-0.392 -29.629,-14.848c-1.249,-10.713 10.049,-22.277 20.695,-21.947c12.09,0.39 18.726,8.603 18.726,14.916c0,7.095 -5.125,13.466 -9.396,13.466c-1.571,0 -2.428,-0.985 -2.493,-2.437c0,-0.717 0.588,-2.427 0.852,-3.345l4.529,-13.863zm-10.18,16.227c-1.053,1.647 -3.023,3.553 -5.193,3.553c-1.25,0 -2.693,-0.397 -3.938,-2.038c-1.118,-1.451 -1.381,-3.024 -1.381,-4.402c0,-2.106 0.983,-5.851 3.155,-8.151c1.445,-1.512 2.826,-2.824 5.515,-2.824c3.422,0 4.471,2.167 4.599,4.006c0.266,2.761 -1.311,7.626 -2.757,9.856zm-17.329,-24.656l-25.291,16.337l-25.293,-16.337l50.584,0zm-0.47,38.115l-56.836,0l0,-36.984l32.015,20.651l17.862,-11.538c-2.006,4.051 -2.919,8.658 -2.381,13.428c0.718,6.254 4.401,11.217 9.34,14.443z" fill="black" id="svg_1"/>
             </g>
            </svg>
        """.trimIndent()

    private val portField = RelayOutputPortField(FIELD_PORT, R.field_port_hint, RequiredStringValidator())
    private val intervalField = NullableBigDecimalField(FIELD_INTERVAL, R.field_interval_hint,
        BigDecimal.ONE, RequiredBigDecimalValidator(), IsIntegerValidator())

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> =
                LinkedHashMap(super.fieldDefinitions)
            result[FIELD_PORT] = portField
            result[FIELD_INTERVAL] = intervalField
            return result
        }

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<State> {
        val portId = extractFieldValue(instance, portField)
        val port = portFinder.searchForOutputPort(Relay::class.java, portId)
        val name = instance.fields[FIELD_NAME]!!
        val interval = extractFieldValue(instance, intervalField)
        return BlinkerAutomationUnit(eventBus, instance, name, states, port, interval.wrapped!!.toInt())
    }

    override val addNewRes = R.configurable_blinker_add

    override val editRes = R.configurable_blinker_edit
    override val parent: Class<out Configurable>
        get() = DevicesConfigurable::class.java

    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = HashMap()
            states[STATE_UNKNOWN] = State.buildReadOnlyState(
                STATE_UNKNOWN,
                R.state_unknown,
            )
            states[STATE_BLINKING] = State.buildControlState(
                STATE_BLINKING,
                R.state_blinking,
                R.action_blinking,
                isSignaled = true
            )
            states[STATE_OFF] = State.buildControlState(
                STATE_OFF,
                R.state_off,
                R.action_off,
            )
            return states
        }

    companion object {
        const val STATE_BLINKING = "on"
        const val STATE_OFF = "off"
        const val FIELD_PORT = "portId"
        const val FIELD_INTERVAL = "interval"
    }
}