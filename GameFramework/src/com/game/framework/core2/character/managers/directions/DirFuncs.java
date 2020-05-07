package com.game.framework.core2.character.managers.directions;

import com.game.framework.core.bodies.Function;

public class DirFuncs {
    private Function<Void, Void> _onDir, _whileDir, _fromDir;

    public DirFuncs setOnDir(Function<Void, Void> fn) {
        _onDir = fn;
        return this;
    }

    public DirFuncs setWhileDir(Function<Void, Void> fn) {
        _whileDir = fn;
        return this;
    }

    public DirFuncs setFromDir(Function<Void, Void> fn) {
        _fromDir = fn;
        return this;
    }

    public void onDir() {
        if (_onDir != null)
            _onDir.call(null);
    }

    public void whileDir() {
        if (_whileDir != null)
            _whileDir.call(null);
    }

    public void fromDir() {
        if (_fromDir != null)
            _fromDir.call(null);
    }
}
