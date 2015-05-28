/*
 * Copyright 2013 the original author or authors.
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
package org.sonarqube.gradle

import org.gradle.listener.ActionBroadcast
import spock.lang.Specification

class SonarRunnerExtensionTest extends Specification {

    def "evaluate properties blocks"() {
        def actionBroadcast = new ActionBroadcast<SonarQubeProperties>()
        def extension = new SonarRunnerExtension(actionBroadcast)
        def props = ["key.1": "value 1"]

        when:
        extension.sonarProperties {
            it.property "key.2", ["value 2"]
            it.properties(["key.3": "value 3", "key.4": "value 4"])
        }

        extension.sonarProperties {
            it.property "key.5", "value 5"
            it.properties["key.2"] << "value 6"
            it.properties.remove("key.3")
        }

        def sonarProperties = new SonarQubeProperties(props)
        actionBroadcast.execute(sonarProperties)

        then:
        props == ["key.1": "value 1", "key.2": ["value 2", "value 6"], "key.4": "value 4", "key.5": "value 5"]
    }
}
