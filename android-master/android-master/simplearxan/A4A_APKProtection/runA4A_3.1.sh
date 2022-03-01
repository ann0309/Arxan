#
#  A4A shell script ver 1.2.0
#  Created by Alan Hsieh on 2019/01/23.
#  Last update on 2019/09/25


########################### Script Feature ###########################

#若不開則自行手動調整Guardspec
AUTO_SET_SIGNATURE=true

#自動使用輸入的APK的SIGNATURE作為Signature Check Guard的參數
# True  
# false 請至手動參數設定調整[keystore_path、keystore_pwd、key_alias、key_pwd]等參數
#       若以上任一參數為空值，將會自動使用Debug的keystore進行設定
AUTO_GET_APK_SIGNATURE=true


DEBUG_MODE=true

########## 此設定請在包版時確認設為 false ##########
REFLECTION_DEBUG=false
################################################

############################# 手動參數設定 #############################
#設置Android sdk路徑
export ANDROID_HOME=~/Library/Android/sdk

#a4a folder path
#A4A protection路徑
#A4A_DIR="/Users/AlanHsieh/Arxan/A4A/3.1.1/a4android-3.1.1-macos-7ddce0ec"
# A4A_DIR="/Users/chimin/Documents/work/A4A/Arxan/A4A/3.1.1"
A4A_DIR="/Users/ann_yang/Desktop/Arxan/practice/android-master/android-master/simplearxan/A4A_APKProtection"

#APK name
# APK_NAME="SimpleArxan"
# apk檔名字
APK_NAME="app-debug" 

#guardspec path
gs_name="guardspec.json"

#keytool
keytool=$JAVA_HOME/bin/keytool

#apk signer
apksigner=~/Library/Android/sdk/build-tools/28.0.3/apksigner

#zipalign
zipalign=~/Library/Android/sdk/build-tools/28.0.3/zipalign

#Debug Keystore
debug_key=~/.android/debug.keystore

#《要Signed才設》Keystore Path
keystore_path=""

#《要Signed才設》Keystore Password
keystore_pwd=""

#《要Signed才設》Key Alias
key_alias=""

#《要Signed才設》Key Password
key_pwd=""

############################## Execute #################################

# Error Handler
function error_handler {
    echo ""
    echo "************  error signal received   ************"
    echo ""
    echo "Stopping the script ......"
    echo ""
    exit
}
trap error_handler ERR

#Print A4A Information(version & expiration date)
$A4A_DIR/bin/secure-dex --license-display | grep "version\|token\|Expiration"

#取得目前sh的根目錄路徑，用來指定dir
dir="$(cd `dirname $0`; pwd)"

#首先刪除out_dir
echo ""
echo "******************************** PRE-STAGE ********************************"
echo ""
echo "Remove last protected result."
rm -rf "$dir/out_dir"
rm -rf "$dir/signed_apk_dir"

echo "Create result folder."
mkdir "$dir/signed_apk_dir"
mkdir "$dir/out_dir"
echo ""

# Set Debug Mode
if [ $DEBUG_MODE == true ]
then
    echo "******************************* DEBUG MODE ON *****************************"
    sed -i '' -e "s/\"debug\": false/\"debug\": true/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"debug\" :false/\"debug\": true/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"debug\":false/\"debug\": true/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"debug\" : false/\"debug\": true/g" $dir/gs_dir/$gs_name
else
    sed -i '' -e "s/\"debug\": true/\"debug\": false/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"debug\" :true/\"debug\": false/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"debug\":true/\"debug\": false/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"debug\" : true/\"debug\": false/g" $dir/gs_dir/$gs_name
fi

if [ $REFLECTION_DEBUG == true ]
then
    echo "*********************** REFLECTION DEBUGGING MODE ON **********************"
    sed -i '' -e "s/\"enableReflectionDebugging\": false/\"enableReflectionDebugging\": true/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"enableReflectionDebugging\" : false/\"enableReflectionDebugging\": true/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"enableReflectionDebugging\":false/\"enableReflectionDebugging\": true/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"enableReflectionDebugging\" : false/\"enableReflectionDebugging\": true/g" $dir/gs_dir/$gs_name
else
    sed -i '' -e "s/\"enableReflectionDebugging\": true/\"enableReflectionDebugging\": false/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"enableReflectionDebugging\" : true/\"enableReflectionDebugging\": false/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"enableReflectionDebugging\":true/\"enableReflectionDebugging\": false/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"enableReflectionDebugging\" : true/\"enableReflectionDebugging\": false/g" $dir/gs_dir/$gs_name
fi

# Auto set signautre
if [ $AUTO_SET_SIGNATURE == true ]
then
    echo ""
    echo "**************************** AUTO SET SIGNATURE ***************************"
    echo ""
    
    # #Replace signature，取得保護前APK的 Signature value，並取代到GuardSpec中的@SIGNATURE@標記。
    # #注意，使用此指令的話APK Protection的sign key與ori_apk的sign key要相同
    echo ""
    echo "Check signed-key SHA-256 value:"
    #######Signature加入點use參數產值（）
	if [ $AUTO_GET_APK_SIGNATURE == true ]
	then
		echo "Use original APK Signature"
		SIGNATURE_VALUE=`$apksigner verify --print-certs $dir/ori_dir/$APK_NAME.apk | grep SHA-256 | cut -d" " -f 6`
	else
		if [ ! "$keystore_path" ] || [ ! "$keystore_pwd" ] || [ ! "$key_alias" ] ||  [ ! "$key_pwd" ]
		then
			echo "Use Debug key to generate Signature"
			SIGNATURE_VALUE=$($keytool -list -v -keystore $debug_key -alias "androiddebugkey" -storepass "android" -keypass "android" 2> /dev/null)
            echo "Test"
		else
			echo "Use Self-defined key to generate Signature"
			SIGNATURE_VALUE=$($keytool -list -v -keystore $keystore_path -alias "$key_alias" -storepass $keystore_pwd -keypass "$key_pwd" 2> /dev/null)
		fi
		SIGNATURE_VALUE=${SIGNATURE_VALUE:407:96}
	    SIGNATURE_VALUE=${SIGNATURE_VALUE//:/}
	    SIGNATURE_VALUE=$(echo $SIGNATURE_VALUE | tr -d ':' | tr -d '\n' )
    fi
	if [ "$SIGNATURE_VALUE" = "" ]
    then
        echo "\nPlease close auto set signature feature if you're input unsigned apk."
        echo "exit..."
        exit
    else
        echo $SIGNATURE_VALUE
        sed -i '' -e "s/@SIGNATURE@/$SIGNATURE_VALUE/g" $dir/gs_dir/$gs_name
    fi
    echo ""
fi

echo ""
echo "***************************** PROTECTION PHASE ****************************"
echo ""
echo "Start protect."
echo "Open $dir/out_dir/protection.log to watch protecting process."

# #apk_protection
$A4A_DIR/bin/secure-dex \
 --input "$dir/ori_dir/$APK_NAME.apk" \
 --blueprint "$dir/gs_dir/$gs_name" \
 --output "$dir/out_dir" &> "$dir/out_dir/protection.log"

echo ""
echo "Protection completed."
echo ""

echo ""
echo "******************************** POST-STAGE *******************************"
echo ""
echo "Start align.."
#sign apk
#align
$zipalign -v -p 4 "$dir/out_dir/$APK_NAME-unaligned-unsigned-protected.apk" "$dir/signed_apk_dir/$APK_NAME-aligned-unsigned-protected.apk" | grep "Verification"

echo ""
echo "Start sign.."
if [ ! "$keystore_path" ] || [ ! "$keystore_pwd" ] || [ ! "$key_alias" ] ||  [ ! "$key_pwd" ]
then
#sign debug
echo "Using debug key"
$apksigner sign --ks $debug_key \
--ks-key-alias "androiddebugkey" --ks-pass pass:"android" --key-pass pass:"android" \
--out "$dir/signed_apk_dir/$APK_NAME-aligned-signed-protected.apk" "$dir/signed_apk_dir/$APK_NAME-aligned-unsigned-protected.apk"
else
#sign release
echo "Using release key"
$apksigner sign --ks "$keystore_path" \
--ks-key-alias "$key_alias" --ks-pass "pass:$keystore_pwd" --key-pass "pass:$key_pwd" \
--out "$dir/signed_apk_dir/$APK_NAME-aligned-signed-protected.apk" "$dir/signed_apk_dir/$APK_NAME-aligned-unsigned-protected.apk"
fi
echo "Signing completed."

if [ $AUTO_SET_SIGNATURE == true ] && [ "$SIGNATURE_VALUE" != "" ]
then
echo ""
echo "Recover Signature Check Guard setting."
# #防護完後將SIGNATURE改回為預設值，以便下一次防護設定
sed -i '' -e "s/$SIGNATURE_VALUE/@SIGNATURE@/g" "$dir/gs_dir/$gs_name"
echo "Recover completed."
echo ""

POST_SIGN_SIGNATURE_VALUE=`$apksigner verify --print-certs "$dir/signed_apk_dir/$APK_NAME-aligned-signed-protected.apk" | grep SHA-256 | cut -d" " -f 6`

    if [ "$POST_SIGN_SIGNATURE_VALUE" != "$SIGNATURE_VALUE" ]
    then
        echo ""
        echo "***************************************************************************"
        echo "********************************* WARNING *********************************"
        echo "***************************************************************************"
        echo "***                                                                     ***"
        echo "***                      Signed key inconsistent!                       ***"
        echo "***              SignatureCheck Guard will be triggered.                ***"
        echo "***                                                                     ***"
        echo "***************************************************************************"
        echo "***************************************************************************"
    fi

fi
echo ""

if [ $DEBUG_MODE == true ]
then
    echo ""
    echo "***************************************************************************"
    echo "********************************* WARNING *********************************"
    echo "***************************************************************************"
    echo "***                                                                     ***"
    echo "***                The APK is protected with debug mode.                ***"
    echo "***                   This APK should not be released!                  ***"
    echo "***                                                                     ***"
    echo "***************************************************************************"
    echo "***************************************************************************"
fi

if [ $REFLECTION_DEBUG == true ]
then
    echo ""
    echo "********************* REFLECTION DEBUGGING MODE START *********************"
    echo ""
    echo "***************************************************************************"
    echo "********************************* WARNING *********************************"
    echo "***************************************************************************"
    echo "***                                                                     ***"
    echo "***                       Protection didn't apply.                      ***"
    echo "***                   This APK should not be released!                  ***"
    echo "***                                                                     ***"
    echo "***************************************************************************"
    echo "***************************************************************************"
    echo ""

    $ANDROID_HOME/platform-tools/adb install -r "$dir/signed_apk_dir/$APK_NAME-aligned-signed-protected.apk"
    python "$A4A_DIR/bin/generate_keepSignatures.py" -o "$dir/gs_dir/proguard-rules.properties"
fi