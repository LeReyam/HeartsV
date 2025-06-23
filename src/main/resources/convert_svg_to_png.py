import os, re
import cairosvg #needed for this to run. if you don't have it: pip install cairosvg


# Edit the folder paths below
svg_folder = r"yourpath/HeartsV/src/main/resources/cardssrc"
png_folder = r"yourpath/HeartsV/src/main/resources/cards"

# Size of PNGs
width, height = 90, 150

# Create the destination folder if it doesn't exist
os.makedirs(png_folder, exist_ok=True)

# Convert SVGs to PNGs
for filename in os.listdir(svg_folder):
    if filename.lower().endswith(".svg"):
        svg_path = os.path.join(svg_folder, filename)

        base_name = filename[:-4]

        if re.match(r"(jack|queen|king)_of_\w+2", base_name):
            base_name = base_name[:-1]

        png_filename = base_name + ".png"
        png_path = os.path.join(png_folder, png_filename)

        print(f"Converting: {filename} → {png_filename}")
        cairosvg.svg2png(url=svg_path, write_to=png_path, output_width=width, output_height=height)

print("✅ All SVGs were converted successfully!")
