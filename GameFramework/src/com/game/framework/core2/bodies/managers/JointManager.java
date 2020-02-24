package com.game.framework.core2.bodies.managers;

import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core2.joints.Joint;

import java.util.ArrayList;
import java.util.List;

public class JointManager {

    private WorldBody body;
    private List<Joint> _joints;

    public JointManager(WorldBody body) {
        this.body = body;
        _joints = new ArrayList<>();
    }

    public void attach(Joint joint) {
        joint.buildOn(body);
        _joints.add(joint);
    }

    public List<Joint> joints() {
        return _joints;
    }

    public void disposeAll() {
        for (Joint joint : _joints) {
            joint.dispose();
        }
    }

}
