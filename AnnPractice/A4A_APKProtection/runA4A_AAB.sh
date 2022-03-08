#若不開則自行手動調整Guardspec
AUTO_SET_SIGNATURE=false

#自動使用輸入的APK的SIGNATURE作為Signature Check Guard的參數
# True  
# false 請至手動參數設定調整[keystore_path、keystore_pwd、key_alias、key_pwd]等參數
#       若以上任一參數為空值，將會自動使用Debug的keystore進行設定
AUTO_GET_APK_SIGNATURE=false

DEBUG_MODE=true

########## 此設定請在包版時確認設為 false ##########
REFLECTION_DEBUG=false
################################################

############################# 手動參數設定 #############################
#設置Android sdk路徑
export ANDROID_HOME=~/Library/Android/sdk

#a4a folder path
A4A_DIR="/Users/wayne_yang/工作/A4A/Arxan各版本dmg+unzip/AllVersion/A4A-4.5.1"

#APK name
AAB_NAME="memory-game"

#guardspec path
gs_name="guardspec.json"

#keytool
keytool=$JAVA_HOME/bin/keytool

#apk signer
apksigner=~/Library/Android/sdk/build-tools/28.0.3/apksigner

#zipalign
zipalign=~/Library/Android/sdk/build-tools/28.0.3/zipalign

#bundle tool
bundletool=bundletool

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
    sed -i '' -e "s/$SIGNATURE_VALUE/@SIGNATURE@/g" "$dir/gs_dir/$gs_name"
    echo "Recover @SIGNATURE@ value sussessful"
    echo ""
    exit
}
# Interrupt Handler
function interrupt_handler {
    echo ""
    echo "************  interrupt signal received   ************"
    echo ""
    echo "Stopping the script ......"
    echo ""
    sed -i '' -e "s/$SIGNATURE_VALUE/@SIGNATURE@/g" "$dir/gs_dir/$gs_name"
    echo "Recover @SIGNATURE@ value sussessful"
    echo ""
    exit
}
trap error_handler ERR
trap interrupt_handler INT

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
rm -rf "$dir/signed_aab_dir"

echo "Create result folder."
mkdir "$dir/signed_aab_dir"
mkdir "$dir/out_dir"
echo ""

# Set Debug Mode
if [ $DEBUG_MODE == true ]
then
    echo "******************************* DEBUG MODE ON *****************************"
    sed -i '' -e "s/\"debug\": false/\"debug\": true/g"  $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"debug\" :false/\"debug\": true/g"  $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"debug\":false/\"debug\": true/g"   $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"debug\" : false/\"debug\": true/g" $dir/gs_dir/$gs_name
else
    sed -i '' -e "s/\"debug\": true/\"debug\": false/g"  $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"debug\" :true/\"debug\": false/g"  $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"debug\":true/\"debug\": false/g"   $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"debug\" : true/\"debug\": false/g" $dir/gs_dir/$gs_name
fi

if [ $REFLECTION_DEBUG == true ]
then
    echo "*********************** REFLECTION DEBUGGING MODE ON **********************"
    sed -i '' -e "s/\"enableReflectionDebugging\": false/\"enableReflectionDebugging\": true/g"  $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"enableReflectionDebugging\" : false/\"enableReflectionDebugging\": true/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"enableReflectionDebugging\": false/\"enableReflectionDebugging\": true/g"  $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"enableReflectionDebugging\" : false/\"enableReflectionDebugging\": true/g" $dir/gs_dir/$gs_name
else
    sed -i '' -e "s/\"enableReflectionDebugging\": true/\"enableReflectionDebugging\": false/g"  $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"enableReflectionDebugging\" : true/\"enableReflectionDebugging\": false/g" $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"enableReflectionDebugging\":true/\"enableReflectionDebugging\": false/g"   $dir/gs_dir/$gs_name
    sed -i '' -e "s/\"enableReflectionDebugging\" : true/\"enableReflectionDebugging\": false/g" $dir/gs_dir/$gs_name
fi

# Auto set signautre
if [ $AUTO_SET_SIGNATURE == true ]
then
    echo ""
    echo "**************************** AUTO SET SIGNATURE ***************************"
    echo ""
    
    # #Replace signature，取得保護前AAB的 Signature value，並取代到GuardSpec中的@SIGNATURE@標記。
    # #注意，使用此指令的話AAB Protection的sign key與ori_aab的sign key要相同
    echo ""
    echo "Check signed-key SHA-256 value:"
    #######Signature加入點use參數產值（）
    if [ ! "$keystore_path" ] || [ ! "$keystore_pwd" ] || [ ! "$key_alias" ] ||  [ ! "$key_pwd" ]
    then
    echo "Use Debug key to generate Signature"
    SIGNATURE_VALUE=$($keytool -list -v -keystore $debug_key -alias "androiddebugkey" -storepass "android" -keypass "android" 2> /dev/null | awk -F "SHA256: " '{print $2}')
        echo "Test"
    else
        echo "Use Self-defined key to generate Signature"
        SIGNATURE_VALUE=$($keytool -list -v -keystore $keystore_path -alias "$key_alias" -storepass $keystore_pwd -keypass "$key_pwd" 2> /dev/null | awk -F "SHA256: " '{print $2}')
    fi
    SIGNATURE_VALUE=${SIGNATURE_VALUE//:/}
    SIGNATURE_VALUE=$(echo $SIGNATURE_VALUE | tr -d ':' | tr -d '\n' )
    
	if [ "$SIGNATURE_VALUE" = "" ]
    then
        echo "\nPlease close auto set signature feature if you're input unsigned aab."
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

# #aab_protection
$A4A_DIR/bin/secure-dex \
 --input "$dir/ori_dir/$AAB_NAME.aab" \
 --blueprint "$dir/gs_dir/$gs_name" \
 --output "$dir/out_dir" &> "$dir/out_dir/protection.log"

echo ""
echo "Protection completed."
echo ""

echo ""
echo "******************************** POST-STAGE *******************************"
echo ""

OUT_AAB=$AAB_NAME-unaligned-unsigned-protected.aab
ALIGNED_AAB=$AAB_NAME-aligned-unsigned-protected.aab
SIGNED_AAB=$AAB_NAME-aligned-signed-protected.aab
SIGNED_APK=$AAB_NAME-aligned-signed-protected.apk
SIGNED_APKS=$AAB_NAME-aligned-signed-protected.apks
SIGNED_ZIP=$AAB_NAME-aligned-signed-protected.zip

echo "Output AAB is at path: $dir/out_dir/$OUT_AAB"

echo "Start align.."

#sign aab
#align
$zipalign -v -p 4 "$dir/out_dir/$OUT_AAB" "$dir/signed_aab_dir/$ALIGNED_AAB" | grep "Verification"

echo "Aligned AAB is at path: $dir/signed_aab_dir/$ALIGNED_AAB"
echo "*Note* This Apk is only aligned, not signed yet."

echo ""
echo "Start sign.."
if [ ! "$keystore_path" ] || [ ! "$keystore_pwd" ] || [ ! "$key_alias" ] ||  [ ! "$key_pwd" ]
then
    #sign debug
    echo "Using debug key"
    $bundletool build-apks --bundle="$dir/out_dir/$OUT_AAB" --output="$dir/signed_aab_dir/$SIGNED_APKS" \
    --mode=universal
    mv "$dir/signed_aab_dir/$SIGNED_APKS" "$dir/signed_aab_dir/$SIGNED_ZIP" 
    unzip "$dir/signed_aab_dir/$SIGNED_ZIP" -d "$dir/signed_aab_dir/$AAB_NAME"
    mv "$dir/signed_aab_dir/$AAB_NAME/universal.apk" "$dir/signed_aab_dir/$AAB_NAME/$SIGNED_APK" 
else
    #sign release
    echo "Using release key"
    cp "$dir/signed_aab_dir/$ALIGNED_AAB" "$dir/signed_aab_dir/$SIGNED_AAB"
    
    jarsigner -verbose -keystore "$keystore_path" -storepass "$keystore_pwd" "$dir/signed_aab_dir/$SIGNED_AAB" "$key_alias"
    echo "jarsigner signing completed."
    echo "Check bundletool Signing information."
    $bundletool build-apks --bundle="$dir/out_dir/$OUT_AAB" --output="$dir/signed_aab_dir/$SIGNED_APKS" \
    --ks "$keystore_path" --ks-key-alias "$key_alias" --ks-pass pass:"$keystore_pwd" --key-pass pass:"$key_pwd"
    echo "Check completed."
fi
echo "Signing completed."

echo "Signed AAB is at path: $dir/signed_aab_dir/$SIGNED_AAB"

if [ $AUTO_SET_SIGNATURE == true ] && [ "$SIGNATURE_VALUE" != "" ]
then
echo ""
echo "Recover Signature Check Guard setting."
# #防護完後將SIGNATURE改回為預設值，以便下一次防護設定
sed -i '' -e "s/$SIGNATURE_VALUE/@SIGNATURE@/g" "$dir/gs_dir/$gs_name"
echo "Recover completed."
echo ""
fi
if [ $DEBUG_MODE == true ]
then
    echo ""
    echo "***************************************************************************"
    echo "********************************* WARNING *********************************"
    echo "***************************************************************************"
    echo "***                                                                     ***"
    echo "***                The AAB is protected with debug mode.                ***"
    echo "***                   This AAB should not be released!                  ***"
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
    echo "***                   This AAB should not be released!                  ***"
    echo "***                                                                     ***"
    echo "***************************************************************************"
    echo "***************************************************************************"
    echo ""

    $bundletool install-apks --apks="$dir/signed_aab_dir/$SIGNED_APKS"
    python "$A4A_DIR/bin/generate_keepSignatures.py" -o "$dir/gs_dir/proguard-rules.properties"
fi

# ========================================================================================

# DATE=$(date +%Y-%m-%d_%H-%M-%S)
# mkdir "$dir/record_aab_dir/$DATE"
# cp "$dir/gs_dir/$gs_name"                   "$dir/record_aab_dir/$DATE/$gs_name"
# cp "$dir/gs_dir/proguard-rules.properties"  "$dir/record_aab_dir/$DATE/proguard-rules.properties"
# cp "$dir/out_dir/DeobfuscationMap.json"     "$dir/record_aab_dir/$DATE/DeobfuscationMap.json"
# cp "$dir/out_dir/protection.log"            "$dir/record_aab_dir/$DATE/protection.log"
# cp "$dir/signed_aab_dir/$SIGNED_AAB"        "$dir/record_aab_dir/$DATE/$SIGNED_AAB"
# cp "$dir/signed_aab_dir/$SIGNED_APKS"       "$dir/record_aab_dir/$DATE/$SIGNED_APKS"