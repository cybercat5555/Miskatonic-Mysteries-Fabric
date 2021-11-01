#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform float GameTime;
uniform int EndPortalLayers;

in vec4 texProj0;
in vec2 texCoord0;
in vec4 vertexColor;

const vec3[] COLORS = vec3[](
    vec3(0.1, 0.1, 0.1),
    vec3(0.2, 0.2, 0.2),
    vec3(0.25, 0.25, 0.25),
    vec3(0.3, 0.4, 0.3),
    vec3(0.3, 0.3, 0.3),
    vec3(0.35, 0.35, 0.35),
    vec3(0.4, 0.4, 0.4),
    vec3(0.45, 0.45, 0.45),
    vec3(0.5, 0.5, 0.5),
    vec3(0.55, 0.55, 0.55),
    vec3(0.6, 0.6, 0.6),
    vec3(0.65, 0.65, 0.65),
    vec3(0.7, 0.7, 0.7),
    vec3(0.8, 0.8, 0.8),
    vec3(0.9, 0.9, 0.9),
    vec3(1.0, 1.0, 1.0)
);

const mat4 SCALE_TRANSLATE = mat4(
    0.5, 0.0, 0.0, 0.25,
    0.0, 0.5, 0.0, 0.25,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
);

mat4 end_portal_layer(float layer) {
    mat4 translate = mat4(
        1.0, 0.0, 0.0, 17.0 / layer,
        0.0, 1.0, 0.0, (2.0 + layer / 1.5) * (GameTime * 1.5),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2_rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));

    mat2 scale = mat2(4.5 - layer / 4.0);

    return mat4(scale * rotate) * translate * SCALE_TRANSLATE;
}

out vec4 fragColor;

void main() {
    if(texture(Sampler0, texCoord0).a < 0.5) {
        discard;
    } else {
        vec3 color = textureProj(Sampler0, texProj0).rgb * COLORS[0];
        for (int i = 0; i < EndPortalLayers; i++) {
            color += textureProj(Sampler1, texProj0 * end_portal_layer(float(i + 1))).rgb * COLORS[i];
        }
        fragColor = vec4(color, 1.0) * vertexColor;
    }
}