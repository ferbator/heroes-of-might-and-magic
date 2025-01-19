package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            Unit candidate = null;
            if (isLeftArmyTarget) {
                for (int i = row.size() - 1; i >= 0; i--) {
                    Unit unit = row.get(i);
                    if (unit != null && unit.isAlive()) {
                        candidate = unit;
                        break;
                    }
                }
            } else {
                for (Unit unit : row) {
                    if (unit != null && unit.isAlive()) {
                        candidate = unit;
                        break;
                    }
                }
            }
            if (candidate != null) {
                suitableUnits.add(candidate);
            }
        }

        return suitableUnits;
    }
}
