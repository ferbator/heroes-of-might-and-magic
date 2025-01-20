package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Map<Unit, Double> efficiencyMap = new HashMap<>();
        for (Unit unit : unitList) {
            double efficiency = ((double) unit.getBaseAttack() / unit.getCost()
                    + (double) unit.getHealth() / unit.getCost());
            efficiencyMap.put(unit, efficiency);
        }

        List<Unit> sortedUnits = new ArrayList<>(unitList);
        sortedUnits.sort((u1, u2) -> Double.compare(efficiencyMap.get(u2), efficiencyMap.get(u1)));

        Army computerArmy = new Army();
        int totalPoints = 0;

        for (Unit prototype : sortedUnits) {
            int maxUnitsForType = Math.min(11, maxPoints / prototype.getCost());
            int count = 0;
            while (count < maxUnitsForType && totalPoints + prototype.getCost() <= maxPoints) {
                Unit newUnit = cloneUnit(prototype);
                computerArmy.getUnits().add(newUnit);
                totalPoints += newUnit.getCost();
                count++;
            }
        }

        computerArmy.setPoints(totalPoints);
        return computerArmy;
    }

    private Unit cloneUnit(Unit prototype) {
        return new Unit(
                prototype.getName(),
                prototype.getUnitType(),
                prototype.getHealth(),
                prototype.getBaseAttack(),
                prototype.getCost(),
                prototype.getAttackType(),
                new HashMap<>(prototype.getAttackBonuses()),
                new HashMap<>(prototype.getDefenceBonuses()),
                prototype.getxCoordinate(),
                prototype.getyCoordinate()
        );
    }
}