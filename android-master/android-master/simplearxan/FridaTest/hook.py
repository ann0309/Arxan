import frida, sys
 
jscode = """
Java.perform(function () {
    var GuardLocation = Java.use('pet.ca.simplearxan.GuardLocation');
    GuardLocation.hdg.implementation = function () {
        send("Hook Start...");
        this.hdg();
        send("Success!")
    }
});
"""
 
def on_message(message, data):
    if message['type'] == 'send':
        print("[*] {0}".format(message['payload']))
    else:
        print(message)

process = frida.get_usb_device().attach('pet.ca.simplearxan')
script = process.create_script(jscode)
script.on('message', on_message)
script.load()
sys.stdin.read()

