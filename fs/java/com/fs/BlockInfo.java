package com.fs;

public class BlockInfo {

    private Long myBlockId;
    private Long nextBlockAddress = -1l;
    private Long remSizeOfCurrentBlock = 0l;

    public BlockInfo(Long myBlockId, Long nextBlockAddress, Long remSizeOfCurrentBlock) {
        this.myBlockId = myBlockId;
        this.nextBlockAddress = nextBlockAddress;
        this.remSizeOfCurrentBlock = remSizeOfCurrentBlock;
    }

    public Long getMyBlockId() {
        return myBlockId;
    }

    public void setMyBlockId(Long myBlockId) {
        this.myBlockId = myBlockId;
    }

    public Long getNextBlockAddress() {
        return nextBlockAddress;
    }

    public void setNextBlockAddress(Long nextBlockAddress) {
        this.nextBlockAddress = nextBlockAddress;
    }

    public Long getRemSizeOfCurrentBlock() {
        return remSizeOfCurrentBlock;
    }

    public void setRemSizeOfCurrentBlock(Long remSizeOfCurrentBlock) {
        this.remSizeOfCurrentBlock = remSizeOfCurrentBlock;
    }

    public void decrementSize(long incBy) {
        remSizeOfCurrentBlock -= incBy;
    }

    @Override
    public String toString() {
        return nextBlockAddress + "," + remSizeOfCurrentBlock;
    }
}
