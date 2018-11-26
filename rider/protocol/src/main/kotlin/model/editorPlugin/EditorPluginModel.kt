package model.editorPlugin

import com.jetbrains.rider.generator.nova.*
import com.jetbrains.rider.generator.nova.PredefinedType.*
import com.jetbrains.rider.generator.nova.csharp.CSharp50Generator
import com.jetbrains.rider.generator.nova.kotlin.Kotlin11Generator

import java.io.File

@Suppress("unused")
object EditorPluginModel: Root() {
    var RdOpenFileArgs = structdef {
        field("path", string)
        field("line", int)
        field("col", int)
    }
    val RdLogEvent = structdef {
        field("time", long)
        field("type", RdLogEventType)
        field("mode", RdLogEventMode)
        field("message", string)
        field("stackTrace", string)
    }

    val RdLogEventType = enum {
        +"Error"
        +"Warning"
        +"Message"
    }

    val RdLogEventMode = enum {
        +"Edit"
        +"Play"
    }

    val TestResult = structdef {
        field("testId", string)
        field("output", string)
        field("duration", int)
        field("status", enum {
            +"Pending"
            +"Running"
            +"Inconclusive"
            +"Ignored"
            +"Success"
            +"Failure"
        })
        field("parentId", string)
    }

    val RunResult = structdef {
        field("passed", bool)
    }

    val TestMode = enum {
        +"Edit"
        +"Play"
    }

    val UnitTestLaunch = classdef {
        field("testNames", immutableList(string))
        field("testGroups", immutableList(string))
        field("testCategories", immutableList(string))
        field("testMode", TestMode)
        sink("testResult", TestResult)
        sink("runResult", RunResult)
        call("abort", void, bool)
    }

    val UnityEditorState = enum {
        +"Disconnected"
        +"Idle"
        +"Play"
        +"Refresh"
    }

    init {
        property("play", bool)
        property("pause", bool)
        source("step", void)

        property("unityPluginVersion", string)
        property("riderProcessId", int)

        property("applicationPath", string)
        property("applicationContentsPath", string)
        property("applicationVersion", string)
        property("scriptingRuntime", int)

        sink("log", RdLogEvent)

        callback("isBackendConnected", void, bool)
        call("getUnityEditorState", void, UnityEditorState)
        callback("openFileLineCol", RdOpenFileArgs, bool)
        call("updateUnityPlugin", string, bool)
        call("refresh", bool, void)
        call("getCompilationResult", void, bool)

        property("unitTestLaunch", UnitTestLaunch)
        source("runUnitTestLaunch", void)

        property("fullPluginPath", string)

        property("editorLogPath", string)
        property("playerLogPath", string)

        sink("notifyIsRecompileAndContinuePlaying", string)
        source("setScriptCompilationDuringPlay", int)
    }
}
