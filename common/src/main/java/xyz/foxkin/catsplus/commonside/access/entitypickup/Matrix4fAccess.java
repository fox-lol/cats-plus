package xyz.foxkin.catsplus.commonside.access.entitypickup;

import net.minecraft.util.math.Vec3f;

public interface Matrix4fAccess {

    /**
     * Gets the translation of the matrix.
     *
     * @return The translation of the matrix.
     */
    Vec3f catsPlus$getTranslation();

    /**
     * Gets the Euler rotation angles of the matrix in radians.
     *
     * @return The Euler rotation angles of the matrix in radians.
     */
    Vec3f catsPlus$getEulerAngles();

    /**
     * The scale of the matrix.
     *
     * @return The scale of the matrix.
     */
    Vec3f catsPlus$getScale();
}
