#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform float Threshold;
uniform float Time;

out vec4 fragColor;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    float distance = sqrt(pow(texCoord.x - 0.5, 2) + pow(texCoord.y - 0.5, 2));
    if(distance > Threshold) {
        distance = (distance - Threshold) * (1/Threshold);
        float xOffset = sin(texCoord.y * distance + Time * 3.1415926535 * 2.0) * 0.01 * distance;
        if(texCoord.x < 0.5) {
            xOffset = xOffset * -1;
        }
        float yOffset = cos(texCoord.x * distance + Time * 3.1415926535 * 2.0) * 0.01 * distance;
        if(texCoord.y < 0.5) {
            yOffset = yOffset * -1;
        }
        vec2 offset = vec2(xOffset, yOffset);
        vec4 rgb = texture(DiffuseSampler, texCoord + offset);
        fragColor = vec4(rgb.rgb, 1.0);
      /*  vec4 blurred = vec4(0.0);
        float totalAlpha = 0.0;
        for(float r = -Radius; r <= Radius; r += 1.0) {
            vec4 sampleValue = texture(DiffuseSampler, texCoord + oneTexel * r * BlurDir);

            // Accumulate average alpha
            totalAlpha = totalAlpha + sampleValue.a;

            // Accumulate smoothed blur
            float strength = 1.0 - abs(r / Radius);
            blurred = blurred + sampleValue;
        }
        vec3 blurredRgb = blurred.rgb / (Radius * 2.0 + 1.0);
        vec4 CurrTexel = texture(DiffuseSampler, texCoord);
        fragColor = vec4(mix(CurrTexel.rgb, blurredRgb, distance), totalAlpha);*/
    }else {
        fragColor = texture(DiffuseSampler, texCoord);
    }
}