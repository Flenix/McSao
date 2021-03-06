package io.github.bfox1.SwordArtOnline.skill;

/**
 * Created by bfox1 on 4/5/2016.
 * Deuteronomy 8:18
 * 1 Peter 4:10
 */
public enum PrimarySkillType
{

    SUPPORT(0, "Support"),
    COMBAT(1, "Combat"),
    WEAPON(2, "Weapon");


    private final int skillID;
    private final String skillName;

    PrimarySkillType(int skillID, String skillName)
    {
        this.skillID = skillID;
        this.skillName = skillName;
    }

    public int getSkillID() {
        return skillID;
    }

    public String getSkillName() {
        return skillName;
    }
}
