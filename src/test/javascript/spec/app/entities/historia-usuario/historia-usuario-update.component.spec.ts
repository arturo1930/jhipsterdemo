/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import HistoriaUsuarioUpdateComponent from '@/entities/historia-usuario/historia-usuario-update.vue';
import HistoriaUsuarioClass from '@/entities/historia-usuario/historia-usuario-update.component';
import HistoriaUsuarioService from '@/entities/historia-usuario/historia-usuario.service';

import TareaService from '@/entities/tarea/tarea.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('HistoriaUsuario Management Update Component', () => {
    let wrapper: Wrapper<HistoriaUsuarioClass>;
    let comp: HistoriaUsuarioClass;
    let historiaUsuarioServiceStub: SinonStubbedInstance<HistoriaUsuarioService>;

    beforeEach(() => {
      historiaUsuarioServiceStub = sinon.createStubInstance<HistoriaUsuarioService>(HistoriaUsuarioService);

      wrapper = shallowMount<HistoriaUsuarioClass>(HistoriaUsuarioUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          historiaUsuarioService: () => historiaUsuarioServiceStub,
          alertService: () => new AlertService(),

          tareaService: () =>
            sinon.createStubInstance<TareaService>(TareaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.historiaUsuario = entity;
        historiaUsuarioServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(historiaUsuarioServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.historiaUsuario = entity;
        historiaUsuarioServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(historiaUsuarioServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundHistoriaUsuario = { id: 123 };
        historiaUsuarioServiceStub.find.resolves(foundHistoriaUsuario);
        historiaUsuarioServiceStub.retrieve.resolves([foundHistoriaUsuario]);

        // WHEN
        comp.beforeRouteEnter({ params: { historiaUsuarioId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.historiaUsuario).toBe(foundHistoriaUsuario);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
