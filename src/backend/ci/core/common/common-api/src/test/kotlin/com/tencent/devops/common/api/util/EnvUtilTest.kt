/*
 * Tencent is pleased to support the open source community by making BK-CI 蓝鲸持续集成平台 available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * BK-CI 蓝鲸持续集成平台 is licensed under the MIT license.
 *
 * A copy of the MIT License is included in this file.
 *
 *
 * Terms of the MIT License:
 * ---------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.devops.common.api.util

import org.junit.Assert
import org.junit.Test

class EnvUtilTest {
    @Test
    fun parseEnvTest() {
        val map = mutableMapOf<String, String>()
        map["age"] = "1"
        map["name"] = "jacky"
        val command = "{\"age\": \${age} , \"sex\": \"boy\", \"name\": \${name}}"
        println(command)
        val parseEnv = EnvUtils.parseEnv(command, map)
        println(parseEnv)

        val command1 = "hello \${{variables.abc}} world"
        val command2 = "\${{variables.abc}}world"
        val command3 = "hello\${{variables.abc}}"
        val command4 = "hello\${{variables.abc"
        val command5 = "hello\${{variables.abc}"
        val command6 = "hello\${variables.abc}}"
        val command7 = "hello\$variables.abc}}"
        val command8 = "echo \${{ variables.hello }}"

        val command9 = "echo \${{ ci.workspace }}"

        val data = mapOf(
            "variables.abc" to "variables.value",
            "variables.hello" to "hahahahaha"
        )

        Assert.assertEquals("hello variables.value world",
            EnvUtils.parseEnv(command1, emptyMap(), contextMap = data))
        Assert.assertEquals("variables.valueworld",
            EnvUtils.parseEnv(command2, emptyMap(), contextMap = data))
        Assert.assertEquals("hellovariables.value",
            EnvUtils.parseEnv(command3, emptyMap(), contextMap = data))
        Assert.assertEquals("hello\${{variables.abc",
            EnvUtils.parseEnv(command4, emptyMap(), contextMap = data))
        Assert.assertEquals("hello\${{variables.abc}",
            EnvUtils.parseEnv(command5, emptyMap(), contextMap = data))
        Assert.assertEquals("hellovariables.value}",
            EnvUtils.parseEnv(command6, emptyMap(), contextMap = data))
        Assert.assertEquals("hello\$variables.abc}}",
            EnvUtils.parseEnv(command7, emptyMap(), contextMap = data))
        Assert.assertEquals("echo hahahahaha",
            EnvUtils.parseEnv(command8, emptyMap(), contextMap = data))
        Assert.assertEquals("echo /data/landun/workspace",
            EnvUtils.parseEnv(
                command = command9,
                data = map,
                replaceWithEmpty = false,
                isEscape = false,
                contextMap = mapOf("ci.workspace" to "/data/landun/workspace")
            ))
    }

    @Test
    fun parseEnvTest1() {
        val map = mutableMapOf<String, String>()
        map["age"] = "1"
        map["name"] = "jacky"
        val command = "{\"age\": \${age} , \"sex\": \"boy\", \"name\": \${name}}"
        println(command)
        val parseEnv = EnvUtils.parseEnv(command, map)
        println(parseEnv)

        val command1 = "hello \${{variables.abc}} world"
        val command2 = "\${{variables.abc}}world"
        val command3 = "hello\${{variables.abc}}"
        val command4 = "hello\${{variables.abc"
        val command5 = "hello\${{variables.abc}"
        val command6 = "hello\${variables.abc}}"
        val command7 = "hello\$variables.abc}}"
        val command8 = "echo \${{ variables.hello }}"

        val command9 = "echo \${{ ci.workspace }} || \${{variables.hello}}"

        val data = mapOf(
            "variables.abc" to "variables.value",
            "variables.hello" to "hahahahaha"
        )

        Assert.assertEquals("hello variables.value world", EnvUtils.parseEnv(command1, data))
        Assert.assertEquals("variables.valueworld", EnvUtils.parseEnv(command2, data))
        Assert.assertEquals("hellovariables.value", EnvUtils.parseEnv(command3, data))
        Assert.assertEquals("hello\${{variables.abc", EnvUtils.parseEnv(command4, data))
        Assert.assertEquals("hello\${{variables.abc}", EnvUtils.parseEnv(command5, data))
        Assert.assertEquals("hellovariables.value}", EnvUtils.parseEnv(command6, data))
        Assert.assertEquals("hello\$variables.abc}}", EnvUtils.parseEnv(command7, data))
        Assert.assertEquals("echo hahahahaha", EnvUtils.parseEnv(command8, data))
        Assert.assertEquals("echo /data/landun/workspace || hahahahaha", EnvUtils.parseEnv(
            command = command9,
            data = data,
            replaceWithEmpty = false,
            isEscape = false,
            contextMap = mapOf("ci.workspace" to "/data/landun/workspace")
        ))
    }
}
