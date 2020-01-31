
/**
* This class is the implementation of the mining node.
* It also plays a role of the full blockchain node.
*
* @author Naohiro Hayashibara
*
*/

import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;
import java.util.UUID;
import java.util.Calendar;

public class MiningNode extends Process
{

    /**
     * The identifier of the process.
     */
    private int id;

    /**
     * The difficulty bits.
     */
    private Integer difficultyBits = 10;

    /**
     * The initial block of the chain.
     */
    private Block initialBlock = null;

    /**
     * The chain of blocks.
     */
    private List<Block> blockChain = new ArrayList<Block>();

    /**
     * The limit of the size of the blockchain.
     */
    private Integer maxSize = 5;

    /**
     * Debug output mode if it is true.
     */
    private Boolean debug = false;

    /**
     * The constructor of the class {@link MiningNode}.
     *
     * @param id the process identifier.
     * @param mq the message queue for message passing.
     */
    public MiningNode(int id, MessageQueue mq) {
        /*
         * call the constructor of superclass.
         */
        super(id, mq);

        this.id = id;
    }

    /**
    * The main procedure of the process.
    * <p> Process 0 creates a miner object and an initial block. Then, it broadcasts them to other nodes.</p>
    */
    public void run() {
        super.run();

        // the procedure of Process 0.
        if (id == 0) {
            Data data = new Data("This is the first block in the chain.");
            initialBlock = new Block(1, 1, 0L, data, new BigInteger("0", 16), new BigInteger("0", 16), Calendar.getInstance().getTime().toString());
            Miner miner = new Miner(initialBlock, difficultyBits); // create a miner.

            initialBlock = miner.createInitialBlock(); // create an initial block of the chain.

            addBlockToChain(initialBlock, blockChain); // add the initial block into the chain.

            broadcastMiner(id, miner);   // send a miner object to others.

            broadcastBlock(id, initialBlock); // send the first block to others.

            Block aBlock;

            while(blockChain.size() < maxSize){
                /*
                ここで以下手順を記述する((1), (2)は順不同)．
                (1) マイニングする
                    (a) minerのgetHashValues()でマイニングしたhash値を得る
                    (b) checkHashValues()でhash値の正当性（基準を満たしているか）を確認する
                    (c) 基準を満たしたhash値があればそのhash値，nonce値など必要な情報をもとに新しいBlockを生成する
                    (d) 自身のblockChainに新しいBlockを追加する(prevHash値は一つ前のBlockのownHash値になっていること)．
                    (e) 他のすべてのプロセスへ新しいBlockを送信
                (2) 他プロセスからBlockを受信する
                    (a) Blockを受信して，送られてきていればそのBlockのhash値が正当かどうかチェック．
                    (b) そのBlockのprevHash値が自身のblockChainのBlockのownHashのいずれかと一致するかどうかチェック．
                    (c) そのBlockのprevHashをもつBlockの後ろに配置
                */

                if (this.checkHashValues(miner.getHashValues()) != null) {
                    // aBlock = new Block(blockNum, diffbits, nonce, data, prevHash, ownHash, ts);

                }

                /*
                * 以下のyield()とsleep()は実行の公平性を保つために消さないように!
                */
                super.yield();
                try{
                    Thread.sleep(1000);
                }
                catch (final InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }

            }

            /*
            * 最後にblockChainの内容を出力する
            */
            this.printChain(id, blockChain);
            this.outputChain(id, blockChain); // output information of each block in the chain.

        }
        // the procedure of processes except Process 0.
        else{
            Object c;
            Block aBlock;
            Miner miner = null; // create a miner.

            while(blockChain.size() < maxSize){
                /*
                ここで以下手順を記述する((1), (2)は順不同)．
                (1) マイニングする
                    (a) minerのgetHashValues()でマイニングしたhash値を得る
                    (b) checkHashValues()でhash値の正当性（基準を満たしているか）を確認する
                    (c) 基準を満たしたhash値があればそのhash値，nonce値など必要な情報をもとに新しいBlockを生成する
                    (d) 自身のblockChainに新しいBlockを追加する(prevHash値は一つ前のBlockのownHash値になっていること)．
                    (e) 他のすべてのプロセスへ新しいBlockを送信
                (2) 他プロセスからBlockを受信する
                    (a) Blockを受信して，送られてきていればそのBlockのhash値が正当かどうかチェック．
                    (b) そのBlockのprevHash値が自身のblockChainのBlockのownHashのいずれかと一致するかどうかチェック．
                    (c) そのBlockのprevHashをもつBlockの後ろに配置
                */


                /*
                * 以下のyield()とsleep()は実行の公平性を保つために消さないように!
                */
                super.yield();
                try{
                    Thread.sleep(1000);
                }
                catch (final InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }

            }

            /*
            * 最後にblockChainの内容を出力する
            */
            this.outputChain(id, blockChain); // output information of each block in the chain.

        }

        return;
    }

    /**
     * Broadcast a miner to every other node.
     *
     * @param id the identifier of the source node.
     * @param aMiner a miner object that is used for mining.
     */
    private void broadcastMiner(Integer id, Miner aMiner) {
        for (Integer count = 0; count < super.getMessageQueue().getTotalNum(); count++) {
            if (count != id) {
                send(count, new DefaultMessage(id, aMiner));
                // System.out.println("send a block to "+count);
            }

        }
    }

    /**
     * Broadcast a block to every other node.
     *
     * @param id    the identifier of the source node.
     * @param aBlock a block that is created by the node.
     */
    private void broadcastBlock(Integer id, Block aBlock) {
        /*
         * 新しいBlockを他のプロセスへブロードキャストする． broadcastMiner()を参考に書くこと．
         */
    }

    /**
     * Receive a block
     *
     * @return Returns {@link Block} if the retruned value of receive() is not null.
     */
    private Block receiveBlock() {
        Object c;
        if ((c = receive()) != null) {
            Block aBlock = (Block) ((Message) c).getContent();
            System.out.println("Proc. " + id + ": received a block of " + aBlock.getOwnHash().toString(16));

            return aBlock;
        } else {
            debugPrint("no message at " + id);

            return null;
        }
    }

    /**
     * Check obtained hash values are valid or not.
     *
     * @param results a list of results that contain a hash value and a nonce.
     * @return Return {@link Result} if {@code results} contain a hash value that
     *         meets the criterion (Miner.isHit() returns {@code true}).
     */
    // results = miner.getHashValues(); // obtain hash values.
    private Result checkHashValues(List<Result> results) {

        for (Result result : results) {

            /*
             * hash値が基準を満たしているかどうか確認する
             */
            if (Miner.isHit(Miner.generatingTarget(result.getDifficultyBits()), result.getHashValue())) {

                /*
                 * 基準を満たしているhash値があればResultとしてリターンする
                 */
                return result;
            }
        }

        return null; // does not hit
    }

    /**
     * Create a block from a given result.
     *
     * @param result a result contains a hash value and a nonce.
     * @param bchain a chain of blocks.
     * @return Returns {@link Block} with {@link Data}, a hash value, a nonce, the
     *         previous hash value, the difficulty bits and a timestamp.
     */
    private Block createBlock(Result result, List<Block> bchain) {
        /*
         * Resultに入っている必要な情報やprevHash値を入れて新しいBlockを作成する
         */
        return null;
    }

    /**
     * Validate the hash value of a received block.
     *
     * @param b a block that has been received.
     * @return Returns {@code boolean} if the block meets the criterion
     *         (Miner.isHit() returns {@code true}).
     */
    private boolean validateBlock(Block aBlock) {
        /*
         * ブロックのハッシュ値が基準を満たしているかチェックする
         */
        return false;
    }

    /**
     * Add a received block into the own chain.
     * <p>
     * Whenever it adds a block to the blockchain, it should check the previous hash
     * value is the same as the hash value of the latest block.
     * </p>
     *
     * @param block  a block that should be added to the blockchain.
     * @param bchain the chain of blocks in the node.
     */
    private void addBlockToChain(Block aBlock, List<Block> bchain) {
        /*
         * 新規BlockをブロックチェーンblockChainに接続する．
         */

    }

    /**
     * Print information of each block in a chain.
     *
     * @param id    the process identifier.
     * @param chain the blockchain of the process.
     */
    private void printChain(Integer id, List<Block> chain) {

        System.out.println("==============" + "Process " + id);
        for (Block blk : chain) {
            System.out.println(blk.getBlockNum() + ": prev_hash = " + blk.getPrevHash().toString(16) + ", own_hash = "
                    + blk.getOwnHash().toString(16));
            // System.out.println(blk.getData().getContent());
        }
        System.out.println("==============");

    }

    /**
     * Output the own chain as a file in the csv format.
     *
     * @param id    the process identifier.
     * @param chain the blockchain of the process.
     */
    private void outputChain(Integer id, List<Block> chain) {
        OutputBlocks anOutputBlocks = new OutputBlocks(id, chain);
        anOutputBlocks.output();
    }

    /**
     * Debug output on a screen.
     *
     * @param aString an string that is appeared in the screen.
     */
    private void debugPrint(String aString) {
        if (debug == true) {
            System.out.println(aString);
        }
    }

}