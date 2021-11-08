package frozenblock.wild.mod.worldgen;

import com.mojang.serialization.Codec;
import frozenblock.wild.mod.blocks.mangrove.MangroveRoots;
import frozenblock.wild.mod.registry.MangroveWoods;
import frozenblock.wild.mod.registry.RegisterWorldgen;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class MangroveTreeRoots extends TreeDecorator {
    public static final MangroveTreeRoots INSTANCE = new MangroveTreeRoots();
    // Our constructor doesn't have any arguments, so we create a unit codec that returns the singleton instance
    public static final Codec<MangroveTreeRoots> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    protected TreeDecoratorType<?> getType() {
        return RegisterWorldgen.MANGROVE_TREE_ROOTS;
    }

    public void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, List<BlockPos> logPositions, List<BlockPos> leavesPositions) {
        // Iterate through block positions
        for (BlockPos logPosition : logPositions) {

            // Pick a value from 0 (inclusive) to 4 (exclusive) and if it's 0, continue
            // This is the chance for spawning the gold block

            // Offset the log position by the resulting side

            if (!world.testBlockState(logPosition.down(4), Predicate.isEqual(MangroveWoods.MANGROVE_LOG.getDefaultState())) && !world.testBlockState(logPosition.down(4), Predicate.isEqual(Blocks.AIR.getDefaultState())) && world.testBlockState(logPosition.down(3), Predicate.isEqual(MangroveWoods.MANGROVE_LOG.getDefaultState())) && world.testBlockState(logPosition.down(2), Predicate.isEqual(MangroveWoods.MANGROVE_LOG.getDefaultState())) && world.testBlockState(logPosition.down(), Predicate.isEqual(MangroveWoods.MANGROVE_LOG.getDefaultState()))) {
                BlockPos targetPosition = logPosition.down().offset(Direction.Axis.X, 1);
                BlockPos currentPosition = targetPosition;
                while (world.testBlockState(currentPosition, Predicate.isEqual(Blocks.AIR.getDefaultState())) || world.testBlockState(currentPosition, Predicate.isEqual(Blocks.WATER.getDefaultState())) || world.testBlockState(currentPosition, Predicate.isEqual(Blocks.CAVE_AIR.getDefaultState()))) {
                    placeRootBlock(currentPosition, world, replacer);
                    currentPosition = currentPosition.down();
                }
                BlockPos posXPos = currentPosition;
                targetPosition = logPosition.down().offset(Direction.Axis.X, -1);
                currentPosition = targetPosition;
                while (world.testBlockState(currentPosition, Predicate.isEqual(Blocks.AIR.getDefaultState())) || world.testBlockState(currentPosition, Predicate.isEqual(Blocks.WATER.getDefaultState())) || world.testBlockState(currentPosition, Predicate.isEqual(Blocks.CAVE_AIR.getDefaultState()))) {
                    placeRootBlock(currentPosition, world, replacer);
                    currentPosition = currentPosition.down();
                }
                BlockPos posXNeg = currentPosition;
                targetPosition = logPosition.down().offset(Direction.Axis.Z, 1);
                currentPosition = targetPosition;
                while (world.testBlockState(currentPosition, Predicate.isEqual(Blocks.AIR.getDefaultState())) || world.testBlockState(currentPosition, Predicate.isEqual(Blocks.WATER.getDefaultState())) || world.testBlockState(currentPosition, Predicate.isEqual(Blocks.CAVE_AIR.getDefaultState()))) {
                    placeRootBlock(currentPosition, world, replacer);
                    currentPosition = currentPosition.down();
                }
                BlockPos posZPos = currentPosition;
                targetPosition = logPosition.down().offset(Direction.Axis.Z, -1);
                currentPosition = targetPosition;
                while (world.testBlockState(currentPosition, Predicate.isEqual(Blocks.AIR.getDefaultState())) || world.testBlockState(currentPosition, Predicate.isEqual(Blocks.WATER.getDefaultState())) || world.testBlockState(currentPosition, Predicate.isEqual(Blocks.CAVE_AIR.getDefaultState()))) {
                    placeRootBlock(currentPosition, world, replacer);
                    currentPosition = currentPosition.down();
                }
                BlockPos posZNeg = currentPosition;
                if (posXPos.getY() <= posXNeg.getY() && posXPos.getY() <= posZPos.getY() && posXPos.getY() <= posZNeg.getY()) {
                    targetPosition = posXPos.offset(Direction.Axis.X, -1);
                } else if (posXNeg.getY() <= posXPos.getY() && posXNeg.getY() <= posZPos.getY() && posXNeg.getY() <= posZNeg.getY()) {
                    targetPosition = posXPos.offset(Direction.Axis.X, 1);
                } else if (posZPos.getY() <= posXPos.getY() && posZPos.getY() <= posXNeg.getY() && posZPos.getY() <= posZNeg.getY()) {
                    targetPosition = posXPos.offset(Direction.Axis.Z, -1);
                } else if (posZNeg.getY() <= posXPos.getY() && posZNeg.getY() <= posXNeg.getY() && posZNeg.getY() <= posZPos.getY()) {
                    targetPosition = posXPos.offset(Direction.Axis.Z, 1);
                }
                targetPosition = targetPosition.up();
                placeRootBottom(targetPosition, world, replacer);
            }
        }
    }
    private void placeRootBlock(BlockPos currentPosition, TestableWorld world, BiConsumer<BlockPos, BlockState> replacer) {
        if (world.testBlockState(currentPosition, Predicate.isEqual(Blocks.AIR.getDefaultState())) || world.testBlockState(currentPosition, Predicate.isEqual(Blocks.CAVE_AIR.getDefaultState()))) {
            replacer.accept(currentPosition, MangroveWoods.MANGROVE_ROOTS.getDefaultState());
        } else if (world.testBlockState(currentPosition, Predicate.isEqual(Blocks.WATER.getDefaultState()))) {
            replacer.accept(currentPosition, MangroveWoods.MANGROVE_ROOTS.getDefaultState().with(MangroveRoots.WATERLOGGED, true));
        }
        if (world.testBlockState(currentPosition.up(), Predicate.isEqual(Blocks.LILY_PAD.getDefaultState()))) {
            replacer.accept(currentPosition.up(), Blocks.AIR.getDefaultState());
        }
    }
    private void placeRootBottom(BlockPos targetPosition, TestableWorld world, BiConsumer<BlockPos, BlockState> replacer) {
        BlockPos currentPosition = targetPosition;
        for (int i = 0; i < 6; i++) {
            placeRootBlock(currentPosition, world, replacer);
            Random rand = new Random();
            int dir = rand.nextInt(4);
            if (dir == 0) {
                currentPosition = currentPosition.offset(Direction.Axis.X, 1);
            } else if (dir == 1) {
                currentPosition = currentPosition.offset(Direction.Axis.X, -1);
            } else if (dir == 2) {
                currentPosition = currentPosition.offset(Direction.Axis.Z, 1);
            } else if (dir == 3) {
                currentPosition = currentPosition.offset(Direction.Axis.Z, -1);
            }
            if (!currentPosition.isWithinDistance(targetPosition, 3)) {
                currentPosition = targetPosition;
                i --;
            }
        }
    }
}


