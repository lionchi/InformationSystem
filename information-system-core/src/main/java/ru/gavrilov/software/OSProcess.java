package ru.gavrilov.software;

import java.io.Serializable;

public class OSProcess implements Serializable {

    private static final long serialVersionUID = 3L;

    private String name = "";
    private String path = "";
    private String commandLine = "";
    private String currentWorkingDirectory = "";
    private String user = "";
    private String userID = "";
    private String group = "";
    private String groupID = "";
    private State state = State.OTHER;
    private int processID;
    private int parentProcessID;
    private int threadCount;
    private int priority;
    private long virtualSize;
    private long residentSetSize;
    private long kernelTime;
    private long userTime;
    private long startTime;
    private long upTime;
    private long bytesRead;
    private long bytesWritten;
    private long openFiles;

    public enum State {
        NEW,
        RUNNING,
        SLEEPING,
        WAITING,
        ZOMBIE,
        STOPPED,
        OTHER
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public String getCommandLine() {
        return this.commandLine;
    }

    public String getCurrentWorkingDirectory() {
        return this.currentWorkingDirectory;
    }

    public String getUser() {
        return this.user;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getGroup() {
        return this.group;
    }

    public String getGroupID() {
        return this.groupID;
    }

    public State getState() {
        return this.state;
    }

    public int getProcessID() {
        return this.processID;
    }

    public int getParentProcessID() {
        return this.parentProcessID;
    }

    public int getThreadCount() {
        return this.threadCount;
    }

    public int getPriority() {
        return this.priority;
    }

    /**
     * @return Возвращает размер виртуальной памяти (VSZ). Он включает всю память
     * что процесс может получить доступ, включая память, которая выгружается
     * и память из разделяемых библиотек.
     */
    public long getVirtualSize() {
        return this.virtualSize;
    }

    /**
     * @return Возвращает размер резидентного набора (RSS). Он используется, чтобы показать, сколько
     * память выделяется для этого процесса и находится в ОЗУ. Это не
     * включить память, которая поменялась. Он включает в себя память из
     * разделяемые библиотеки, если страницы из этих библиотек
     * фактически в памяти. Он включает в себя всю стек и кучу памяти.
     */
    public long getResidentSetSize() {
        return this.residentSetSize;
    }

    /**
     * @return Возвращает количество миллисекунд, которое процесс выполнил в системном режиме.
     */
    public long getKernelTime() {
        return this.kernelTime;
    }

    /**
     * @return Возвращает количество миллисекунд, которое процесс выполнил в пользовательском режиме.
     */
    public long getUserTime() {
        return this.userTime;
    }

    /**
     *
     * @return возвращает количество миллисекунд с момента запуска процесса
     */
    public long getUpTime() {
        if (this.upTime < this.kernelTime + this.userTime) {
            return this.kernelTime + this.userTime;
        }
        return this.upTime;
    }

    /**
     *
     * @return Возвращает время начала процесса в миллисекундах с 1 января 1970 года.
     */
    public long getStartTime() {
        return this.startTime;
    }

    public long getBytesRead() {
        return this.bytesRead;
    }

    public long getBytesWritten() {
        return this.bytesWritten;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }

    public void setCurrentWorkingDirectory(String currentWorkingDirectory) {
        this.currentWorkingDirectory = currentWorkingDirectory;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public void setParentProcessID(int parentProcessID) {
        this.parentProcessID = parentProcessID;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setVirtualSize(long virtualSize) {
        this.virtualSize = virtualSize;
    }

    public void setResidentSetSize(long residentSetSize) {
        this.residentSetSize = residentSetSize;
    }

    public void setKernelTime(long kernelTime) {
        this.kernelTime = kernelTime;
    }

    public void setUserTime(long userTime) {
        this.userTime = userTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setUpTime(long upTime) {
        this.upTime = upTime;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public void setBytesWritten(long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }

    public void setOpenFiles(long count){
        this.openFiles = count;
    }

    public long getOpenFiles(){
        return openFiles;
    }

}
