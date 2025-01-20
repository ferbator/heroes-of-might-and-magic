package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        while (hasLivingUnits(playerArmy) && hasLivingUnits(computerArmy)) {
            List<Unit> allUnits = new ArrayList<>();
            allUnits.addAll(playerArmy.getUnits().stream().filter(Unit::isAlive).toList());
            allUnits.addAll(computerArmy.getUnits().stream().filter(Unit::isAlive).toList());

            allUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            for (Unit unit : allUnits) {
                if (!unit.isAlive()) continue;
                Unit target = unit.getProgram().attack();
                printBattleLog.printBattleLog(unit, target);
            }
        }
    }

    private boolean hasLivingUnits(Army army) {
        return army.getUnits().stream().anyMatch(Unit::isAlive);
    }
}