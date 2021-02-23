#version 110

uniform sampler2D DiffuseSampler;

// Time in seconds (+ tick delta)
uniform float STime;
uniform vec2 Frequency;
uniform vec2 WobbleAmount;
varying vec2 texCoord;

void main() {
    float xOffset = sin(texCoord.y * Frequency.x + STime * 3.1415926535 * 2.0) * WobbleAmount.x;
    float yOffset = cos(texCoord.x * Frequency.y + STime * 3.1415926535 * 2.0) * WobbleAmount.y;
    vec2 offset = vec2(xOffset, yOffset);
    vec4 rgb = texture2D(DiffuseSampler, texCoord + offset);
    gl_FragColor = rgb;
    //texCoord.x = texCoord.x + sin(STime);
}