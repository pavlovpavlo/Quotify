package com.kovhan.core.ui.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ModelViewIntent<State, Effect>(
    initialState: State
) : MVI<State, Effect> {

    private val mviScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val uiStateFlow = MutableStateFlow(initialState)
    override val uiState: StateFlow<State> = uiStateFlow.asStateFlow()

    private val uiEffectChannel = Channel<Effect>(Channel.BUFFERED)
    override val uiEffect: Flow<Effect> = uiEffectChannel.receiveAsFlow()

    override fun publishEffect(effect: Effect) {
        mviScope.launch { uiEffectChannel.send(effect) }
    }

    override fun publishState(transformation: State.() -> State) {
        uiStateFlow.update {
            transformation(it)
        }
    }

    override fun cancelMviScope() {
        mviScope.cancel()
    }
}
