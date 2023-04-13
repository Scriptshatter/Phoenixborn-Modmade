uniform vec3 highlight_color; // the color of the highlight
uniform float highlight_radius; // the radius of the highlight
uniform sampler2D sampler0; // the game's default texture sampler

void main() {
    vec4 texel = texture2D(sampler0, gl_TexCoord[0].xy); // get the color of the current pixel

    if (texel.a == 0.0) { // if the pixel is transparent, skip it
        discard;
    }

    vec3 block_pos = gl_ModelViewMatrixInverse[3].xyz; // get the position of the current block

    float dist = distance(block_pos, gl_FragCoord.xyz); // calculate the distance between the block and the camera

    if (dist <= highlight_radius && (texel.r == 1.0 || texel.g == 1.0 || texel.b == 1.0)) { // if the block is within the radius and is an ore block
        gl_FragColor = vec4(highlight_color, 1.0); // highlight the block
    } else {
        gl_FragColor = texel; // otherwise, use the default color
    }
}