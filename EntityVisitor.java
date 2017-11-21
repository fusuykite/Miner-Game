public interface EntityVisitor<R>
    {
        R visit(ORE ore);

        R visit(BlackSmith blackSmith);

        R visit(Obstacle obstacle);

        R visit(VEIN vein);

        R visit(Quake quake);

        R visit(ORE_BLOB ore_blob);

        R visit(MINER_FULL miner_full);

        R visit(MINER_NOT_FULL miner_not_full);

    }

