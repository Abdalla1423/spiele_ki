from PIL import Image, ImageDraw, ImageFont
import re

def fen_to_board(fen):
    print(fen)
    # Split the input string by '/' to separate rows
    rows = fen.split('/')

    # Add '1' to the beginning and end of the first row
    rows[0] = '1' + rows[0] + '1'

    # Add '1' to the beginning and end of the last row
    rows[-1] = '1' + rows[-1] + '1'

    # Initialize an empty 8x8 board
    board = [['' for _ in range(8)] for _ in range(8)]

    # Process each row from bottom to top
    for row_idx, row in enumerate(reversed(rows)):
        col_idx = 0  # Start at the beginning of each row
        i = 0  # Index for iterating through the characters in the row string
        while i < len(row):
            char = row[i]
            if char.isdigit():
                # If the character is a digit, it represents empty spaces
                num_empty_spaces = int(char)
                for _ in range(num_empty_spaces):
                    board[row_idx][col_idx] = ''
                    col_idx += 1
            elif char == 'b' or char == 'r':
                # If the character is 'b' or 'r', check for stacked pieces
                if i + 1 < len(row) and row[i + 1] in 'br':
                    board[row_idx][col_idx] = char + row[i + 1]
                    i += 1  # Skip the next character since it's part of the stack
                else:
                    board[row_idx][col_idx] = char
                col_idx += 1
            i += 1

    return board
    # Split the input string by '/' to separate rows
    rows = fen.split('/')

    # Initialize an empty 8x8 board
    board = [['' for _ in range(8)] for _ in range(8)]

    # Process each row
    for row_idx, row in enumerate(rows):
        col_idx = 0  # Start at the beginning of each row
        i = 0  # Index for iterating through the characters in the row string
        while i < len(row):
            char = row[i]
            if char.isdigit():
                # If the character is a digit, it represents empty spaces
                num_empty_spaces = int(char)
                for _ in range(num_empty_spaces):
                    board[row_idx][col_idx] = ''
                    col_idx += 1
            elif char == 'b' or char == 'r':
                # If the character is 'b' or 'r', check for stacked pieces
                if i + 1 < len(row) and row[i + 1] in 'br':
                    board[row_idx][col_idx] = char + row[i + 1]
                    i += 1  # Skip the next character since it's part of the stack
                else:
                    board[row_idx][col_idx] = char
                col_idx += 1
            i += 1

    return board

def draw_board(board, cell_size=50, image_number=0):
    # Create a blank image with space for labels
    img_width = cell_size * 10  # Extra space for labels and the non-playable corners
    img_height = cell_size * 9  # Extra space for labels and the non-playable corners
    img = Image.new('RGB', (img_width, img_height), color='white')
    draw = ImageDraw.Draw(img)

    # Colors for the circles
    colors = {
        'b': 'blue',
        'r': 'red'
    }

    # Offset for stacked pieces
    offset = 10

    # Draw the grid and the circles based on the board state
    for row in range(8):
        for col in range(8):
            # Skip the non-playable corners
            if (row == 0 and col in [0, 7]) or (row == 7 and col in [0, 7]):
                continue

            x = (col + 1) * cell_size
            y = (row + 1) * cell_size
            draw.rectangle([x, y, x + cell_size, y + cell_size], outline='black')

            cell_value = board[row][col]
            if cell_value:
                if cell_value in colors:
                    draw.ellipse([x + 5, y + 5, x + cell_size - 5, y + cell_size - 5], fill=colors[cell_value])
                elif cell_value == 'bb':
                    draw.ellipse([x + 5 + offset, y + 5 + offset, x + cell_size - 5, y + cell_size - 5], fill='blue')
                    draw.ellipse([x + 5, y + 5, x + cell_size - 5 - offset, y + cell_size - 5 - offset], fill='blue')
                elif cell_value == 'rr':
                    draw.ellipse([x + 5 + offset, y + 5 + offset, x + cell_size - 5, y + cell_size - 5], fill='red')
                    draw.ellipse([x + 5, y + 5, x + cell_size - 5 - offset, y + cell_size - 5 - offset], fill='red')
                elif cell_value == 'br':
                    draw.ellipse([x + 5 + offset, y + 5 + offset, x + cell_size - 5, y + cell_size - 5], fill='red')
                    draw.ellipse([x + 5, y + 5, x + cell_size - 5 - offset, y + cell_size - 5 - offset], fill='blue')
                elif cell_value == 'rb':
                    draw.ellipse([x + 5 + offset, y + 5 + offset, x + cell_size - 5, y + cell_size - 5], fill='blue')
                    draw.ellipse([x + 5, y + 5, x + cell_size - 5 - offset, y + cell_size - 5 - offset], fill='red')

    # Draw the row and column labels
    font = ImageFont.load_default()
    for i in range(8):
        # Draw row numbers
        draw.text((5, (i + 1) * cell_size + cell_size / 3), str(i + 1), fill='black', font=font)
        # Draw column letters
        draw.text(((i + 1) * cell_size + cell_size / 3, 5), chr(ord('A') + i), fill='black', font=font)
        # Save the image
    img.save(f'board_with_grid_{image_number}.jpg')

def extract_board_states(file_path):
    board_states = []
    with open(file_path, 'r') as file:
        for line in file:
            match = re.search(r'([1-8b0r\/]+ [br])', line)
            if match:
                board_states.append(match.group(1))
    return board_states

def main(file_path):
    # board_fen_strings = extract_board_states(file_path)
    # for idx, fen_string in enumerate(board_fen_strings):
        # board = fen_to_board(fen_string[:-2])
        # draw_board(board, image_number=idx)
    board = fen_to_board("b01b0b0b0b0/1b0b01b01b01/3b01b02/2b05/8/2r0r01rr2/1r04r01/r0r0r0r0r0r0")
    draw_board(board, 200)

#fen_string = "6/1bb1b0bbb0b01/r02b04/2b01b0b02/2r02r02/1r02rrr02/6rr1/2r01r01"
#board = fen_to_board(fen_string)
#draw_board(board)
# Replace 'your_file.txt' with the path to your text file
#file_path = '../jump-sturdy-game-server/00361965.txt'
# main(file_path)

board = fen_to_board("b01b0b0b0b0/1b0b01b01b01/3b01b02/2b05/8/2r0r01rr2/1r04r01/r0r0r0r0r0r0")
draw_board(board)


