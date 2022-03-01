package pet.ca.simplearxan;

    //Arxan防護進入點
public class GuardLocation {


    /*
        cg  =   Custom Guard
        rdg =   Root Detection
        hdg =   Hook Detection
        edg =   Emulator Detection
        fdg =   Dynamic Instrumentation Detection(Frida)
        ddg =   Debug Detection
        csg =   Checksum
        scg =   Signature Check
        rvg =   Resource Verification
        mpg =   Malicious Package Detection
        vdg =   Virtualization Detection
     */
    public static void cg() {
//        cg_drm();
        cg_debugMode();
        cg_usbConnect();
    }

    public static void cg_drm(){

    }

    public static void cg_usbConnect(){

    }

    public static void cg_debugMode(){

    }

    public static void rdg() {
        rdg1();
        rdg2();
        rdg3();
        rdg4();
        rdg5();
        rdg6();
    }

    private static void rdg1() {

    }

    private static void rdg2() {

    }

    private static void rdg3() {

    }

    private static void rdg4() {

    }

    private static void rdg5() {

    }

    private static void rdg6() {

    }

    public static void hdg() {
        hdg1();
        hdg2();
        hdg3();
        hdg4();
    }

    private static void hdg1() {

    }

    private static void hdg2() {

    }

    private static void hdg3() {

    }

    private static void hdg4() {

    }

    public static void edg() {

    }

    public static void fdg() {

    }

    public static void ddg() {

    }

    public static void csg() {

    }

    public static void scg() {

    }

    public static void rvg() {

    }

    public static void mpg(){

    }

    public static void vdg(){
        vdg1();
        vdg2();
        vdg3();
        vdg4();
        vdg5();
        vdg6();
        vdg7();
    }

    private static void vdg1(){

    }

    private static void vdg2(){

    }

    private static void vdg3(){

    }

    private static void vdg4(){

    }

    private static void vdg5(){

    }

    private static void vdg6(){

    }

    private static void vdg7(){

    }

    private static void print(String s){
        System.out.println(s);
    }

}
