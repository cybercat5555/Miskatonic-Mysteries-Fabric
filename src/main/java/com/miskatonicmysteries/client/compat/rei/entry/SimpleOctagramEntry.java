package com.miskatonicmysteries.client.compat.rei.entry;

public class SimpleOctagramEntry {}//todo
     /*extends RecipeEntry {
    private static final Comparator<EntryStack> ENTRY_COMPARATOR = Comparator.comparingLong(EntryStack::hashCode);
    private List<Slot> inputWidgets;

    protected SimpleOctagramEntry(List<List<EntryStack>> input) {
        this.inputWidgets = simplify(input).stream().filter(stacks -> !stacks.isEmpty()).map(stacks -> Widgets.createSlot(new Point(0, 0)).entries(stacks).disableBackground().disableHighlight().disableTooltips()).collect(Collectors.toList());
    }

    private static List<List<EntryStack>> simplify(List<List<EntryStack>> original) {
        Map<List<EntryStack>, AtomicReference<Fraction>> inputCounter = Maps.newLinkedHashMap();
        original.stream().collect(Collectors.groupingBy(stacks -> CollectionUtils.mapAndMax(stacks, EntryStack::getAccurateAmount, Fraction::compareTo).orElse(Fraction.empty())))
                .forEach((fraction, value) -> {
                    if (!fraction.equals(Fraction.empty())) {
                        value.forEach(stackList -> {
                            List<EntryStack> stacks = inputCounter.keySet().stream().filter(s -> equalsList(stackList, s)).findFirst().orElse(stackList);
                            AtomicReference<Fraction> reference = inputCounter.computeIfAbsent(stacks, s -> new AtomicReference<>(Fraction.empty()));
                            reference.set(reference.get().add(fraction));
                        });
                    }
                });
        return inputCounter.entrySet().stream().map(entry -> CollectionUtils.map(entry.getKey(), stack -> {
            EntryStack s = stack.copy();
            s.setAmount(entry.getValue().get());
            return s;
        })).collect(Collectors.toList());
    }


    public static RecipeEntry from(Supplier<List<List<EntryStack>>> input) {
        return from(input.get());
    }

    public static RecipeEntry from(List<List<EntryStack>> input) {
        return new SimpleOctagramEntry(input);
    }

    public static boolean equalsList(List<EntryStack> list_1, List<EntryStack> list_2) {
        List<EntryStack> stacks_1 = list_1.stream().distinct().sorted(ENTRY_COMPARATOR).collect(Collectors.toList());
        List<EntryStack> stacks_2 = list_2.stream().distinct().sorted(ENTRY_COMPARATOR).collect(Collectors.toList());
        if (stacks_1.equals(stacks_2))
            return true;
        if (stacks_1.size() != stacks_2.size())
            return false;
        for (int i = 0; i < stacks_1.size(); i++)
            if (!stacks_1.get(i).equalsIgnoreTagsAndAmount(stacks_2.get(i)))
                return false;
        return true;
    }

    @Override
    public void render(MatrixStack matrices, Rectangle bounds, int mouseX, int mouseY, float delta) {
        int xx = bounds.x + 4, yy = bounds.y + 2;
        int j = 0;
        int itemsPerLine = getItemsPerLine();
        for (Slot entryWidget : inputWidgets) {
            entryWidget.setZ(getZ() + 50);
            entryWidget.getBounds().setLocation(xx, yy);
            entryWidget.render(matrices, mouseX, mouseY, delta);
            xx += 18;
            j++;
            if (j >= itemsPerLine) {
                yy += 18;
                xx = bounds.x + 4;
                j = 0;
            }
        }
    }

    @Nullable
    @Override
    public Tooltip getTooltip(Point point) {
        for (Slot widget : inputWidgets) {
            if (widget.containsMouse(point))
                return widget.getCurrentTooltip(point);
        }
        return null;
    }

    @Override
    public int getHeight() {
        return 4 + getItemsHeight() * 18;
    }

    public int getItemsHeight() {
        return MathHelper.ceil(((float) inputWidgets.size()) / (getItemsPerLine()));
    }

    public int getItemsPerLine() {
        return 4;
    }
}*/