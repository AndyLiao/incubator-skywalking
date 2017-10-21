/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package org.skywalking.apm.plugin.sjdbc.define;

import com.dangdang.ddframe.rdb.sharding.executor.ExecutorEngine;
import org.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceConstructorInterceptor;
import org.skywalking.apm.plugin.sjdbc.ExecuteEventListener;

/**
 * {@link ExecutorEngineConstructorInterceptor} enhances {@link ExecutorEngine#}'s constructor, initializing {@link ExecuteEventListener}
 * 
 * @author gaohongtao
 */
public class ExecutorEngineConstructorInterceptor implements InstanceConstructorInterceptor {
    
    @Override public void onConstruct(EnhancedInstance objInst, Object[] allArguments) {
        ExecuteEventListener.init();
    }
}