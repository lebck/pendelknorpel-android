package de.hsrm.lback.pendelknorpel.domains.location.views.overview;

import android.support.annotation.Nullable;

import de.hsrm.lback.pendelknorpel.domains.location.models.Location;

public class LocationOverviewStateMachine {
    private Location src;
    private Location target;
    private boolean srcGps;
    private boolean targetGps;
    private Integer uid;
    private Step currentStep;
    @Nullable private OnChangeCallback onChange;

    public LocationOverviewStateMachine() {
        currentStep = Step.SOURCE;
        srcGps = false;
        targetGps = false;
    }

    public void setSrc(Location src) {
        this.src = src;

        if (target == null) {
            this.currentStep = Step.TARGET;
        } else {
            this.currentStep = Step.DONE;
        }

        update();
    }

    public void setTarget(Location target) {
        this.target = target;

        if (src == null) {
            this.currentStep = Step.SOURCE;
        } else {
            this.currentStep = Step.DONE;
        }

        update();
    }

    public void setEditState(int uid) {
        currentStep = Step.EDIT;
        this.uid = uid;
        update();
    }

    public void setBothState() {
        src = null;
        target = null;
        currentStep = Step.SOURCE;
        update();
    }

    private void update() {
        if (onChange != null) {
            switch (currentStep) {
                case SOURCE:
                    this.onChange.handleSourceState(srcGps);
                    break;
                case TARGET:
                    this.onChange.handleTargetState(targetGps);
                    break;
                case DONE:
                    this.onChange.handleDoneState(src, target);
                    this.reset();
                    break;
                case EDIT:
                    onChange.handleEditState(this.uid);
                    this.reset();
                    break;
            }
        }
    }

    public void reset () {
        this.src = null;
        this.target = null;
        this.srcGps = false;
        this.targetGps = false;
        this.uid = null;
        this.currentStep = Step.SOURCE;
    }

    public void setStateChangeCallback(OnChangeCallback ready) {
        this.onChange = ready;
    }

    public void setSrcGps(boolean srcGps) {
        this.srcGps = srcGps;
    }

    public void setTargetGps(boolean targetGps) {
        this.targetGps = targetGps;
    }

    enum Step {
        SOURCE, TARGET, DONE, EDIT
    }

    interface OnChangeCallback {
        void handleSourceState(boolean gps);
        void handleTargetState(boolean gps);
        void handleDoneState(Location src, Location target);
        void handleEditState(int uid);
    }


}
