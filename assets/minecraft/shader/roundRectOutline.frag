#version 120

uniform vec2 location, rectSize;
uniform vec4 color, outlineColor;
uniform float radius, outlineThickness;

float roundedSDF(vec2 centerPos, vec2 size, float radius) {
    return length(max(abs(centerPos) - size + radius, 0.0)) - radius;
}

float roundedBoxSDF(vec2 centerPos, vec2 size, float radius) {
    return length(max(abs(centerPos) -size + radius, 0.)) - radius;
}

void main() {
    float distance = roundedBoxSDF((rectSize * .5) - (gl_TexCoord[0].st * rectSize), (rectSize * .5) - 2.8, radius);

    float blendAmount = smoothstep(0., 2., abs(distance) - (outlineThickness * .5));

    vec4 insideColor = (distance < 0.) ? color : vec4(outlineColor.rgb,  0.0);
    gl_FragColor = mix(outlineColor, insideColor, blendAmount);

}