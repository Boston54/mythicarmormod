package me.boston.mythicarmor.util;

import net.minecraft.world.item.component.DyedItemColor;

public class RGB {
    public int r;
    public int g;
    public int b;

    public RGB(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static RGB of(int rgb) {
        int b = rgb & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int r = rgb >> 16 & 0xFF;
        return new RGB(r, g, b);
    }

    @Override
    public String toString() {
        return "R: " + r + ", G: " + g + ", B: " + b;
    }

    public int toInt() {
        int rgb = r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;
        return rgb;
    }

    public int toOpaqueInt() {
        return toInt() | 0xFF000000;
    }

    public DyedItemColor toDyedItemColor() {
        return new DyedItemColor(toInt(), false);
    }

    public RGB copy() {
        return new RGB(r, g, b);
    }

    public RGB addInPlace(RGB rgb) {
        r += rgb.r;
        g += rgb.g;
        b += rgb.b;
        return this;
    }

    public RGB add(RGB rgb) {
        return copy().addInPlace(rgb);
    }

    public RGB multiplyInPlace(double x) {
        r =(int)(r * x);
        g =(int)(g * x);
        b =(int)(b * x);
        return this;
    }

    public RGB multiply(double x) {
        return copy().multiplyInPlace(x);
    }
}