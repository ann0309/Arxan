#include "EnsureIT.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

using namespace eit;
#define SEED 1

int main(int argc, char *argv[]) {
    GuardSpec gs;

    gs.obfuscate(gs.sourceBitcode(), 10);

    gs.detectRoot(
                    /*name */ "L1_rdg_root",
                    /*invoc*/gs.function("Java_pet_ca_simplearxan_JNIUtils_root").marker("_Z4rootv")
            ).addAlgorithm("root")
            .setNonTamperAction(gs.function("_Z19rootNonTamperActionv"))
            .setTamperAction(gs.function("_Z16rootTamperActionv"));

    gs.detectRoot(
                    /*name */ "L1_rdg_rootManagementApps",
                    /*invoc*/gs.function("Java_pet_ca_simplearxan_JNIUtils_root").marker(
                            "_Z18rootManagementAppsv")
            ).addAlgorithm("rootManagementApps")
            .setNonTamperAction(gs.function("_Z33rootManagementAppsNonTamperActionv"))
            .setTamperAction(gs.function("_Z30rootManagementAppsTamperActionv"));

    gs.detectRoot(
                    /*name */ "L1_rdg_rootHidingApps",
                    /*invoc*/gs.function("Java_pet_ca_simplearxan_JNIUtils_root").marker(
                            "_Z14rootHidingAppsv")
            ).addAlgorithm("rootHidingApps")
            .setNonTamperAction(gs.function("_Z29rootHidingAppsNonTamperActionv"))
            .setTamperAction(gs.function("_Z26rootHidingAppsTamperActionv"));

    gs.detectRoot(
                    /*name */ "L1_rdg_rootRequiringApps",
                    /*invoc*/gs.function("Java_pet_ca_simplearxan_JNIUtils_root").marker(
                            "_Z17rootRequiringAppsv")
            ).addAlgorithm("rootRequiringApps")
            .setNonTamperAction(gs.function("_Z32rootRequiringAppsNonTamperActionv"))
            .setTamperAction(gs.function("_Z29rootRequiringAppsTamperActionv"));

    RangeExpr &range_func_main =
            gs.function("Java_pet_ca_simplearxan_JNIUtils_checksum") +
            gs.function("Java_pet_ca_simplearxan_JNIUtils_debugger") +
            gs.function("Java_pet_ca_simplearxan_JNIUtils_fdg") +
            gs.function("Java_pet_ca_simplearxan_JNIUtils_hook") +
            gs.function("Java_pet_ca_simplearxan_JNIUtils_root") +
            gs.function("Java_pet_ca_simplearxan_JNIUtils_setEnv") +
            gs.function("_Z14rootHidingAppsv") +
            gs.function("_Z15fdgTamperActionv") +
            gs.function("_Z16hookTamperActionv") +
            gs.function("_Z16rootTamperActionv") +
            gs.function("_Z17rootRequiringAppsv") +
            gs.function("_Z18fdgNonTamperActionv") +
            gs.function("_Z18rootManagementAppsv") +
            gs.function("_Z19hookNonTamperActionv") +
            gs.function("_Z19rootNonTamperActionv") +
            gs.function("_Z20checksumTamperActionv") +
            gs.function("_Z20debuggerTamperActionv") +
            gs.function("_Z23checksumNonTamperActionv") +
            gs.function("_Z23debuggerNonTamperActionv") +
            gs.function("_Z26rootHidingAppsTamperActionv") +
            gs.function("_Z29rootHidingAppsNonTamperActionv") +
            gs.function("_Z29rootRequiringAppsTamperActionv") +
            gs.function("_Z30rootManagementAppsTamperActionv") +
            gs.function("_Z32rootRequiringAppsNonTamperActionv") +
            gs.function("_Z33rootManagementAppsNonTamperActionv") +
            gs.function("_Z4rootv") +
            gs.function("_ZN7_JNIEnv17GetStaticMethodIDEP7_jclassPKcS3_") +
            gs.function("_ZN7_JNIEnv20CallStaticVoidMethodEP7_jclassP10_jmethodIDz") +
            gs.function("_ZN7_JNIEnv9FindClassEPKc");

    gs.detectHooking(
                    /*name */ "L1_hdg_1",
                    /*invoc*/ gs.function("Java_pet_ca_simplearxan_JNIUtils_hook").entry(),
                    /*range*/ range_func_main
            ).addCommonlyHookedFunctions()
            .setNonTamperAction(gs.function("_Z19hookNonTamperActionv"))
            .setTamperAction(gs.function("_Z16hookTamperActionv"));


    gs.checksum(
                    /*name */ "L1_csg_1",
                    /*invoc*/ gs.function("Java_pet_ca_simplearxan_JNIUtils_checksum").entry(),
                    /*range*/ gs.sourceBitcode()
            )
            .setNonTamperAction(gs.function("_Z23checksumNonTamperActionv"))
            .setTamperAction(gs.function("_Z20checksumTamperActionv"));

    gs.detectDebugger(
                    /*name */ "L1_ddg_1",
                    /*invoc*/ gs.function("Java_pet_ca_simplearxan_JNIUtils_debugger").entry()
            )
            .setNonTamperAction(gs.function("_Z23debuggerNonTamperActionv"))
            .setTamperAction(gs.function("_Z23debuggerNonTamperActionv"));
    gs.detectDebugger(
                    /*name */ "L1_ddg_2",
                    /*invoc*/ gs.function("Java_pet_ca_simplearxan_JNIUtils_debugger").entry()
            )
            .setNonTamperAction(gs.function("_Z23debuggerNonTamperActionv"))
            .setTamperAction("fail");

    std::set <eit::DIDAlgorithm> algorithms;
    algorithms.insert(eit::FridaPresenceDetectionQuickScan);

    gs.detectDynamicInstrumentation(
                    /*name     */ "L1_fdg_1",
                    /*invoc    */ gs.function("Java_pet_ca_simplearxan_JNIUtils_fdg").entry(),
                    /*algorithm*/ algorithms
            )
            .setNonTamperAction(gs.function("_Z18fdgNonTamperActionv"))
            .setTamperAction(gs.function("_Z15fdgTamperActionv"));
    gs.seed(SEED);
    gs.execute(argc, argv);

    return 0;
}