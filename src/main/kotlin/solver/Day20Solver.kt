package solver

import Day


@Day(2023, 20)
class Day20Solver : BaseSolver(), ISolver {
    companion object {
        var modulesThatNeedToBeHigh = mutableMapOf<String, Int>()
        var clickCount = 0
    }

    interface Module {
        val name: String
        fun receivePulse(io: Boolean, origin: String): List<Pulse>

        fun addModuleDestination(module: Module)

        fun getState(): Boolean
    }

    class FlipFlop(override val name: String) : Module {
        private var state: Boolean = false
        private val moduleList = mutableListOf<Module>()

        override fun receivePulse(io: Boolean, origin: String): List<Pulse> {
            return if (io) {
                listOf()
            } else {
                val listOfPulses = mutableListOf<Pulse>()

                state = !state

                for (module in moduleList) {
                    listOfPulses.add(Pulse(name, state, module.name))
                }

                listOfPulses
            }
        }

        override fun addModuleDestination(module: Module) {
            moduleList.add(module)
        }

        override fun getState(): Boolean {
            return state
        }
    }

    class Conjunction(override val name: String) : Module {
        private val inputState = mutableMapOf<String, Boolean>()
        private val moduleList = mutableListOf<Module>()
        private var state = true

        override fun receivePulse(io: Boolean, origin: String): List<Pulse> {
            val listOfPulses = mutableListOf<Pulse>()
            inputState[origin] = io

            state = !inputState.values.all { it }

            if (modulesThatNeedToBeHigh.containsKey(name) && state && modulesThatNeedToBeHigh[name] == 0) {
                modulesThatNeedToBeHigh[name] = clickCount
            }
            for (module in moduleList) {
                listOfPulses.add(Pulse(name, state, module.name))
            }

            return listOfPulses
        }

        override fun addModuleDestination(module: Module) {
            moduleList.add(module)
        }

        fun addInput(name: String) {
            inputState[name] = false
        }

        override fun getState(): Boolean {
            return state
        }
    }

    class Broadcaster : Module {
        private val moduleList = mutableListOf<Module>()
        override val name: String
            get() = "broadcaster"

        override fun receivePulse(io: Boolean, origin: String): List<Pulse> {
            val listOfPulses = mutableListOf<Pulse>()
            for (module in moduleList) {
                listOfPulses.add(Pulse(name, io, module.name))
            }
            return listOfPulses
        }

        override fun addModuleDestination(module: Module) {
            moduleList.add(module)
        }

        override fun getState(): Boolean {
            return false
        }
    }

    data class Pulse(val origin: String, val value: Boolean, val destination: String)

    class Button(val moduleList: Set<Module>) {
        private val pulseQueue = mutableListOf<Pulse>()
        var pulseCountPos = 0
        var pulseCountNeg = 0

        fun press() {
            clickCount++
            pulseQueue.add(Pulse("button", false, "broadcaster"))
            handleQueue()
        }

        fun handleQueue() {
            while (pulseQueue.isNotEmpty()) {
                val pulse = pulseQueue.removeFirst()
                val nextPulses =
                    moduleList.first { it.name == pulse.destination }.receivePulse(pulse.value, pulse.origin)
                if (pulse.value) {
                    pulseCountPos++
                } else {
                    pulseCountNeg++
                }
                pulseQueue.addAll(nextPulses)
            }
        }
    }

    class FinalMachine : Module {
        override val name: String
            get() = "rx"

        private var state = false

        override fun receivePulse(io: Boolean, origin: String): List<Pulse> {
            state = io
            return listOf()
        }

        override fun addModuleDestination(module: Module) {
            return
        }

        override fun getState(): Boolean {
            return state
        }


    }

    override fun solve(cookie: String?): Pair<String, String?> {
        val lines = getLines(cookie)

        val finalMachine = FinalMachine()
        val moduleList = mutableMapOf<Module, List<String>>(
            finalMachine to listOf()
        )
        val broadcaster = Broadcaster()

        for (line in lines) {
            if (line.isBlank()) {
                continue
            }
            val split = line.split(" -> ")
            val name = split.first().substring(1)
            val outputs = split.last().split(", ")
            val module = if (line.startsWith("broadcaster")) {
                broadcaster
            } else if (line.startsWith("%")) {
                FlipFlop(name)
            } else {
                Conjunction(name)
            }

            moduleList[module] = outputs
        }

        for (module in moduleList.keys) {
            val outputs = moduleList[module]!!.map { mod ->
                var mdl = moduleList.keys.firstOrNull {
                    it.name == mod
                }
                if (mdl == null) {
                    mdl = FlipFlop(mod)
                    moduleList[mdl] = listOf()
                }

                mdl
            }

            for (output in outputs) {
                module.addModuleDestination(output)

                if (output is Conjunction) {
                    output.addInput(module.name)
                }
            }
        }

        val button = Button(moduleList.keys)

        val deliversToFinal = moduleList.keys.find { moduleList[it]!!.contains("rx") }
        moduleList.keys.filter { moduleList[it]!!.contains(deliversToFinal!!.name) }.forEach {
            modulesThatNeedToBeHigh[it.name] = 0
        }


        var part1 = 0
        var part2 = 0

        while (modulesThatNeedToBeHigh.values.any { it == 0 }) {
            button.press()

            if (clickCount == 1000) {
                part1 = button.pulseCountPos * button.pulseCountNeg
            }
        }

        return Pair(
            part1.toString(),
            modulesThatNeedToBeHigh.values.fold(1.toLong()) { acc, i -> acc * i.toLong() }.toString()
        )
    }
}