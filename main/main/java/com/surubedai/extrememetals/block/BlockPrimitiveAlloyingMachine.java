package com.surubedai.extrememetals.block;

import com.surubedai.extrememetals.blockentity.BlockentityPrimitiveAlloyingMachine;
import com.surubedai.extrememetals.regi.ExtremeMetalsAddBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockPrimitiveAlloyingMachine extends BaseEntityBlock {
    //作動中かどうか
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    // 💡 1. ブロックの向きを管理するプロパティを定義（水平方向の4方向：北・南・東・西）
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockPrimitiveAlloyingMachine(Properties properties) {
        super(properties);
        // 💡 2. 初期状態（デフォルトの向き）を「北」に設定しておく
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(LIT, false)
        );
    }

    // 💡 3. プレイヤーがブロックを設置するとき、プレイヤーの向きの「逆」を向くようにする（正面がプレイヤー側を向く）
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
            .setValue(FACING, context.getHorizontalDirection().getOpposite())
            .setValue(LIT, false);
    }

    // 💡 4. ブロックを回転させるコマンド（構造物生成など）に対応させる
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    // 💡 5. ブロックを反転させるコマンドに対応させる
    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    // 💡 6. Minecraftに「このブロックは FACING プロパティを持っています」と登録する
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(LIT);
    }

    // Blockクラスの中にこれを追記
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL; // 通常のブロックモデルとして描画・処理する
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ExtremeMetalsAddBlockEntities.PRIMITIVE_ALLOYING_MACHINE.get().create(pPos, pState);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null; // サーバーのみ

        return createTickerHelper(type, ExtremeMetalsAddBlockEntities.PRIMITIVE_ALLOYING_MACHINE.get(),
                (pLevel, pPos, pState, pBlockEntity) -> pBlockEntity.tick(pLevel, pPos, pState));
    }


    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BlockentityPrimitiveAlloyingMachine crusher) {
                // Capability経由でアイテムを取り出してドロップ
                crusher.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
                    }
                });
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) { // サーバー側でのみ処理する
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BlockentityPrimitiveAlloyingMachine alloyingMachine) {
                NetworkHooks.openScreen((ServerPlayer) player, alloyingMachine, pos);
            } else {
                throw new IllegalStateException("私たちのBlockEntityコンテナがありません！");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

}
