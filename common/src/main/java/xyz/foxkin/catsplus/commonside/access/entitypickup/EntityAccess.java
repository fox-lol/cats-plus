package xyz.foxkin.catsplus.commonside.access.entitypickup;

public interface EntityAccess {

    /**
     * Gets the held pose number. Different numbers mean the
     * entity will be seen held in a different pose.
     *
     * @return The held pose number.
     */
    int catsPlus$getHeldPoseNumber();

    /**
     * Sets the held pose number.
     *
     * @param heldPoseNumber The held pose number.
     */
    void catsPlus$setHeldPoseNumber(int heldPoseNumber);
}
